package org.jeecg.generate.listener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BeidouImageServer {
    private static final int PORT = 8888;
    private static final int PACKET_SIZE = 200 * 1024; // 200KB
    private static final int TRANSMISSION_INTERVAL = 15; // 15秒

    public static void main(String[] args) throws IOException {
        // 读取原始图像
        BufferedImage originalImage = ImageIO.read(new File("D:\\Desktop\\jwd\\1743413911969.jpg"));

        // 创建图像金字塔（不同分辨率的图像）
        List<BufferedImage> pyramid = createImagePyramid(originalImage, 5);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("北斗图像服务器启动，等待连接...");
            System.out.println("传输参数: 每包" + PACKET_SIZE / 1024 + "KB，间隔" + TRANSMISSION_INTERVAL + "秒");

            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端已连接");

            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

            // 先发送金字塔的层级数量
            oos.writeInt(pyramid.size());
            oos.flush();

            // 依次发送各层级图像（从模糊到清晰）
            for (int level = 0; level < pyramid.size(); level++) {
                BufferedImage image = pyramid.get(level);

                // 将图像转换为Base64编码的字节数组
                byte[] imageData = imageToByteArray(image);

                // 分割为多个数据包
                List<ImgPacket> packets = splitIntoPackets(imageData, level, pyramid.size());

                System.out.println("开始发送第 " + (level + 1) + " 层，共 " + packets.size() + " 个包");

                // 发送当前层级的所有包
                for (ImgPacket packet : packets) {
                    oos.writeObject(packet);
                    oos.flush();
                    System.out.println("已发送包 " + packet.getPacketId() + "/" + packet.getTotalPackets());

                    // 等待指定的传输间隔
                    TimeUnit.SECONDS.sleep(TRANSMISSION_INTERVAL);
                }

                // 等待客户端确认接收完成
                boolean ready = ois.readBoolean();
                if (!ready) {
                    System.out.println("客户端未准备好，传输终止");
                    break;
                }

                System.out.println("第 " + (level + 1) + " 层传输完成");
            }

            System.out.println("所有图像数据传输完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建图像金字塔
    private static List<BufferedImage> createImagePyramid(BufferedImage original, int levels) {
        List<BufferedImage> pyramid = new ArrayList<>();
        pyramid.add(original);

        BufferedImage current = original;
        for (int i = 1; i < levels; i++) {
            int newWidth = current.getWidth() / 2;
            int newHeight = current.getHeight() / 2;

            // 缩小图像，产生更模糊的版本
            BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaled.createGraphics();
            g.drawImage(current, 0, 0, newWidth, newHeight, null);
            g.dispose();

            pyramid.add(scaled);
            current = scaled;
        }

        // 反转列表，让模糊的图像在前面
        List<BufferedImage> reversed = new ArrayList<>();
        for (int i = pyramid.size() - 1; i >= 0; i--) {
            reversed.add(pyramid.get(i));
        }
        return reversed;
    }

    // 将图像转换为字节数组
    private static byte[] imageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return Base64.getEncoder().encode(baos.toByteArray());
    }

    // 分割数据为多个数据包
    private static List<ImgPacket> splitIntoPackets(byte[] data, int level, int totalLevels) {
        List<ImgPacket> packets = new ArrayList<>();
        int totalPackets = (int) Math.ceil((double) data.length / PACKET_SIZE);

        for (int i = 0; i < totalPackets; i++) {
            int start = i * PACKET_SIZE;
            int length = Math.min(PACKET_SIZE, data.length - start);

            byte[] packetData = new byte[length];
            System.arraycopy(data, start, packetData, 0, length);

            boolean isLastPacket = (i == totalPackets - 1);
            packets.add(new ImgPacket(i + 1, totalPackets, level, totalLevels, packetData, isLastPacket));
        }

        return packets;
    }
}

// 图像数据包类
class ImagePacket implements Serializable {
    private int packetId;
    private int totalPackets;
    private int level;
    private int totalLevels;
    private byte[] data;
    private boolean isLastPacket;

    public ImagePacket(int packetId, int totalPackets, int level, int totalLevels, byte[] data, boolean isLastPacket) {
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

