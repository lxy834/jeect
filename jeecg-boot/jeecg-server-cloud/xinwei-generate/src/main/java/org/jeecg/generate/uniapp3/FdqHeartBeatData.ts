import { render } from '@/common/renderUtils';
//列表数据
export const columns = [
    {
    title: 'GPS时间',
    align:"center",
    dataIndex: 'gpsTime'
   },
   {
    title: '运行时长',
    align:"center",
    dataIndex: 'runningTime'
   },
   {
    title: '心跳频率',
    align:"center",
    dataIndex: 'heartRate'
   },
   {
    title: '系统温度',
    align:"center",
    dataIndex: 'systemTemp'
   },
   {
    title: '外部电压',
    align:"center",
    dataIndex: 'externalVoltage'
   },
   {
    title: '内部电压',
    align:"center",
    dataIndex: 'internalVoltage'
   },
   {
    title: '信号质量',
    align:"center",
    dataIndex: 'commQuality'
   },
   {
    title: '固件版本',
    align:"center",
    dataIndex: 'appVersion'
   },
   {
    title: '车牌号',
    align:"center",
    dataIndex: 'plateNumber'
   },
];