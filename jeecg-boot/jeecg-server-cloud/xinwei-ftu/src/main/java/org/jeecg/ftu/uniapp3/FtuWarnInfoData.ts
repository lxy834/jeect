import { render } from '@/common/renderUtils';
//列表数据
export const columns = [
    {
    title: '告警信息',
    align:"center",
    dataIndex: 'warnInfo'
   },
   {
    title: '告警类别',
    align:"center",
    dataIndex: 'deviceType_dictText'
   },
   {
    title: '设备id',
    align:"center",
    dataIndex: 'deviceId'
   },
   {
    title: '告警时间',
    align:"center",
    dataIndex: 'warnTime'
   },
   {
    title: '设备名称',
    align:"center",
    dataIndex: 'deviceName'
   },
   {
    title: '点位',
    align:"center",
    dataIndex: 'insLocation'
   },
   {
    title: '线路位置',
    align:"center",
    dataIndex: 'lineLocation'
   },
];