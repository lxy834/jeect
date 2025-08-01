import {defHttp} from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/ftu/ftuDevice/list',
  getIndexList = '/ftu/ftuDevice/getIndexList',
  volume = '/ftu/ftuDevice/volume',
  bind = '/ftu/ftuDevice/bind',
  submitBind = '/ftu/ftuDevice/submit/bind',
  getVolumeById = '/ftu/ftuDevice/getVolumeById',
  save ='/ftu/ftuDevice/add',
  edit ='/ftu/ftuDevice/edit',
  deleteOne = '/ftu/ftuDevice/delete',
  deleteBatch = '/ftu/ftuDevice/deleteBatch',
  importExcel = '/ftu/ftuDevice/importExcel',
  exportXls = '/ftu/ftuDevice/exportXls',
  ftuF411DeviceList = '/ftu/ftuDevice/queryFtuF411DeviceByMainId',
  ftuElectlVolumeList = '/ftu/ftuDevice/queryFtuElectlVolumeByMainId',
  ftuWarnInfoList = '/ftu/ftuDevice/queryFtuWarnInfoByMainId',
}
/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;

export const volume = Api.volume;

export const bind = Api.bind;

export const submitBind = Api.submitBind;

export const getIndexList = Api.getIndexList;

export const getVolumeById = Api.getVolumeById;

/**
 * 导入api
 */
export const getImportUrl = Api.importExcel;
/**
 * 查询子表数据
 * @param params
 */
export const ftuF411DeviceList = Api.ftuF411DeviceList;
/**
 * 查询子表数据
 * @param params
 */
export const ftuElectlVolumeList = Api.ftuElectlVolumeList;
/**
 * 查询子表数据
 * @param params
 */
export const ftuWarnInfoList = Api.ftuWarnInfoList;
/**
 * 列表接口
 * @param params
 */
export const list = (params) =>
  defHttp.get({url: Api.list, params});

/**
 * 删除单个
 */
export const deleteOne = (params,handleSuccess) => {
  return defHttp.delete({url: Api.deleteOne, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const batchDelete = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.deleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({url: url, params});
}
