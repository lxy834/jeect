import { render } from '@/common/renderUtils';
//列表数据
export const columns = [
    {
    title: '资产车牌',
    align:"center",
    dataIndex: 'plateNumber'
   },
   {
    title: '工单类型',
    align:"center",
    dataIndex: 'orderType'
   },
   {
    title: '目标用户',
    align:"center",
    dataIndex: 'targetUser'
   },
   {
    title: '市局',
    align:"center",
    dataIndex: 'deptName'
   },
   {
    title: '地区局',
    align:"center",
    dataIndex: 'bureau'
   },
   {
    title: '工单状态',
    align:"center",
    dataIndex: 'orderStatus'
   },
   {
    title: '发电时间',
    align:"center",
    dataIndex: 'beginTime'
   },
   {
    title: '结束时间',
    align:"center",
    dataIndex: 'endTime'
   },
   {
    title: '发电量',
    align:"center",
    dataIndex: 'stat'
   },
   {
    title: '油耗',
    align:"center",
    dataIndex: 'fuel'
   },
   {
    title: '行驶里程',
    align:"center",
    dataIndex: 'mileage'
   },
];