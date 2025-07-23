package org.jeecg.generate.listener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BeidouImageClient {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 8888;
    private JLabel imageLabel;
    private JTextArea statusArea;
    private JLabel progressLabel;
    private BufferedImage currentImage;
    private int totalLevels;
    private int currentLevel = 0;

    // 用于缓存当前层级的数据包
    private Map<Integer, byte[]> currentLevelPackets = new HashMap<>();
    private int expectedPackets = 0;
    private int receivedPackets = 0;

    // 用于与前端通信（Base64编码的图像数据）
    private String currentImageBase64;

    public BeidouImageClient() {
        // 确保UI在EDT线程中初始化
        SwingUtilities.invokeLater(this::initUI);
    }

    public static void main(String[] args) {
        // 创建客户端实例
        BeidouImageClient client = new BeidouImageClient();

        // 在单独线程中启动数据接收，避免阻塞UI
        new Thread(client::start).start();
    }

    private void initUI() {
        JFrame frame = new JFrame("北斗图像渐进传输客户端");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // 图像显示区域
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        // 状态和进度区域
        JPanel statusPanel = new JPanel(new BorderLayout());

        progressLabel = new JLabel("等待接收数据...");
        statusPanel.add(progressLabel, BorderLayout.NORTH);

        statusArea = new JTextArea(5, 40);
        statusArea.setEditable(false);
        statusPanel.add(new JScrollPane(statusArea), BorderLayout.CENTER);

        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_IP, PORT)) {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            // 读取总层级数
            totalLevels = ois.readInt();
            appendStatus("将接收 " + totalLevels + " 层级的图像数据");
            appendStatus("每包数据大小：200KB，传输间隔：15秒");

            // 接收数据
            while (currentLevel < totalLevels) {
                ImgPacket packet = (ImgPacket) ois.readObject();
                processPacket(packet);

                // 如果是当前层级的最后一个包，通知服务器可以发送下一层级
                if (packet.isLastPacket() && packet.getLevel() == currentLevel) {
                    oos.writeBoolean(true);
                    oos.flush();
                    currentLevel++;
                    currentLevelPackets.clear();
                    receivedPackets = 0;
                    expectedPackets = 0;
                    appendStatus("准备接收第 " + (currentLevel + 1) + " 层数据");
                }
            }

            appendStatus("所有图像数据接收完成");
        } catch (Exception e) {
            e.printStackTrace();
            appendStatus("连接错误: " + e.getMessage());
        }
    }

    private void processPacket(ImgPacket packet) {
        try {
            // 初始化当前层级的预期包数
            if (packet.getPacketId() == 1) {
                expectedPackets = packet.getTotalPackets();
                appendStatus("开始接收第 " + (packet.getLevel() + 1) + " 层，共 " + expectedPackets + " 个包");
            }

            // 存储数据包
            currentLevelPackets.put(packet.getPacketId(), packet.getData());
            receivedPackets++;

            // 更新进度 - 使用invokeLater确保UI更新
            final int progress = (int) (((double) receivedPackets / expectedPackets) * 100);
            final int level = packet.getLevel();
            final int totalLevels = packet.getTotalLevels();

            SwingUtilities.invokeLater(() -> {
                progressLabel.setText(String.format("第 %d/%d 层 - 接收进度: %d%% (%d/%d 包)",
                        level + 1, totalLevels,
                        progress, receivedPackets, expectedPackets));
            });

            appendStatus("收到数据包 " + packet.getPacketId() + "/" + expectedPackets);

            // 如果已收到所有包，重组图像
            if (receivedPackets == expectedPackets) {
                byte[] imageData = reconstructData(currentLevelPackets, expectedPackets);
                // 解码Base64数据
                byte[] decodedData = Base64.getDecoder().decode(imageData);
                BufferedImage newImage = ImageIO.read(new ByteArrayInputStream(decodedData));

                // 更新当前图像
                updateImage(newImage);

                // 转换为Base64供前端使用
                currentImageBase64 = imageToBase64(currentImage);
                appendStatus("第 " + (packet.getLevel() + 1) + " 层图像重组完成");
            }
        } catch (Exception e) {
            appendStatus("处理数据包错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 重组数据包
    private byte[] reconstructData(Map<Integer, byte[]> packets, int totalPackets) {
        // 计算总数据长度
        int totalLength = 0;
        for (byte[] data : packets.values()) {
            totalLength += data.length;
        }

        // 重组数据
        byte[] result = new byte[totalLength];
        int position = 0;

        for (int i = 1; i <= totalPackets; i++) {
            byte[] packetData = packets.get(i);
            if (packetData != null) {
                System.arraycopy(packetData, 0, result, position, packetData.length);
                position += packetData.length;
            }
        }

        return result;
    }

    // 将图像转换为Base64编码
    private String imageToBase64(BufferedImage image) throws IOException {
        if (image == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        byte[] imageInBytes = baos.toByteArray();
        baos.close();

        return Base64.getEncoder().encodeToString(imageInBytes);
    }

    private void updateImage(BufferedImage newImage) {
        if (currentImage == null) {
            currentImage = newImage;
        } else {
            // 如果新图像更清晰（尺寸更大），则替换
            if (newImage.getWidth() > currentImage.getWidth() ||
                    newImage.getHeight() > currentImage.getHeight()) {
                currentImage = newImage;
            } else {
                // 否则融合图像
                Graphics2D g = currentImage.createGraphics();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g.drawImage(newImage, 0, 0, null);
                g.dispose();
            }
        }

        // 确保在EDT线程中更新图像
        SwingUtilities.invokeLater(() -> {
            ImageIcon icon = new ImageIcon(currentImage);
            imageLabel.setIcon(icon);
        });
    }

    // 添加状态信息 - 确保在EDT线程中执行
    private void appendStatus(String message) {
        SwingUtilities.invokeLater(() -> {
            statusArea.append(message + "\n");
            statusArea.setCaretPosition(statusArea.getDocument().getLength());
        });
    }
}

// 需要单独定义ImagePacket类
class ImgPacket implements Serializable {
    private int packetId;
    private int totalPackets;
    private int level;
    private int totalLevels;
    private byte[] data;
    private boolean isLastPacket;

    public ImgPacket(int packetId, int totalPackets, int level, int totalLevels, byte[] data, boolean isLastPacket) {
        this.packetId = packetId;
        this.totalPackets = totalPackets;
        this.level = level;
        this.totalLevels = totalLevels;
        this.data = data;
        this.isLastPacket = isLastPacket;
    }

    // Getters
    public int getPacketId() {
        return packetId;
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isLastPacket() {
        return isLastPacket;
    }
}
