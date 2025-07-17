<template>
  <div>
    <!--引用表格-->
   <BasicTable @register="registerTable" :rowSelection="rowSelection">
     <!--插槽:table标题-->
      <template #tableTitle>
          <a-button type="primary" v-auth="'ftu:ftu_f411_device:add'"  @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
          <a-button  type="primary" v-auth="'ftu:ftu_f411_device:exportXls'"  preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
          <j-upload-button  type="primary" v-auth="'ftu:ftu_f411_device:importExcel'"  preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
              <template #overlay>
                <a-menu>
                  <a-menu-item key="1" @click="batchHandleDelete">
                    <Icon icon="ant-design:delete-outlined"></Icon>
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
              <a-button v-auth="'ftu:ftu_f411_device:deleteBatch'">批量操作
                <Icon icon="mdi:chevron-down"></Icon>
              </a-button>
        </a-dropdown>
        <!-- 高级查询 -->
        <super-query :config="superQueryConfig" @search="handleSuperQuery" />
      </template>
       <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)"></TableAction>
        <a-button v-show="record.ftuId===null || record.ftuId===''" type="link" size="small" @click="getFTU(record)">绑定</a-button>
      </template>
      <!--字段回显插槽-->
      <template v-slot:bodyCell="{ column, record, index, text }">
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <FtuF411DeviceModal @register="registerModal" @success="handleSuccess"></FtuF411DeviceModal>


    <el-dialog
      v-model="state.dialog"
      title="设备绑定"
      width="500"
      center
      draggable
    >
      <el-select
        v-model="state.submit.ftuId"
        filterable
        placeholder="选择或搜索FTU"
      >
        <el-option
          v-for="item in state.tableData"
          :key="item.deviceName"
          :label="item.deviceName"
          :value="item.id"
        />
      </el-select>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="state.dialog = false">取消</el-button>
          <el-button v-auth="'ftu:ftu_device:bind'" type="primary" @click="save">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" name="ftu-ftuF411Device" setup>
  import {ref, reactive, computed, unref} from 'vue';
  import {BasicTable, useTable, TableAction} from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage'
  import {useModal} from '/@/components/Modal';
  import FtuF411DeviceModal from './components/FtuF411DeviceModal.vue'
  import {columns, searchFormSchema, superQuerySchema} from './FtuF411Device.data';
  import {list, deleteOne, batchDelete, getImportUrl,getExportUrl} from './FtuF411Device.api';
  import { defHttp } from "@/utils/http/axios";
  import { bind,submitBind } from "@/views/ftu/list/FtuDevice.api.ts";
  import {downloadFile} from '/@/utils/common/renderUtils';
  const { createMessage, createConfirm } = useMessage();
  import { useUserStore } from '/@/store/modules/user';
  import { useMessage } from "@/hooks/web/useMessage";
  const queryParam = reactive<any>({});
  const checkedKeys = ref<Array<string | number>>([]);
  const userStore = useUserStore();
  const state = reactive({
    dialog:false,
    tableData:[],
    submit:{
      ftuId:'',
      id:''
    }
  })
  //注册model
  const [registerModal, {openModal}] = useModal();
   //注册table数据
  const { prefixCls,tableContext,onExportXls,onImportXls } = useListPage({
      tableProps:{
           title: '通信终端',
           api: list,
           columns,
           canResize:false,
           formConfig: {
                //labelWidth: 120,
                schemas: searchFormSchema,
                autoSubmitOnEnter:true,
                showAdvancedButton:true,
                fieldMapToNumber: [
                ],
                fieldMapToTime: [
                ],
            },
           actionColumn: {
               width: 120,
               fixed:'right'
           },
           beforeFetch: (params) => {
             return Object.assign(params, queryParam);
           },
        },
        exportConfig: {
            name:"通信终端",
            url: getExportUrl,
            params: queryParam,
        },
        importConfig: {
            url: getImportUrl,
            success: handleSuccess
        },
    })

  const [registerTable, {reload},{ rowSelection, selectedRowKeys }] = tableContext

  // 高级查询配置
  const superQueryConfig = reactive(superQuerySchema);

  function getFTU(v){
    defHttp.get({url:bind}).then(res =>{
      state.tableData = res
      state.submit.id = v.id
      state.dialog = true
    })
  }

  function save(){
    let params = state.submit
    defHttp.post({url: submitBind, params}).then(res =>{
      createMessage.success('绑定成功');
      state.dialog = false
      reload()
    });
  }

  /**
   * 高级查询事件
   */
  function handleSuperQuery(params) {
    Object.keys(params).map((k) => {
      queryParam[k] = params[k];
    });
    reload();
  }

   /**
    * 新增事件
    */
  function handleAdd() {
     openModal(true, {
       isUpdate: false,
       showFooter: true,
     });
  }
   /**
    * 编辑事件
    */
  function handleEdit(record: Recordable) {
     openModal(true, {
       record,
       isUpdate: true,
       showFooter: true,
     });
   }
   /**
    * 详情
   */
  function handleDetail(record: Recordable) {
     openModal(true, {
       record,
       isUpdate: true,
       showFooter: false,
     });
   }
   /**
    * 删除事件
    */
  async function handleDelete(record) {
     await deleteOne({id: record.id}, handleSuccess);
   }
   /**
    * 批量删除事件
    */
  async function batchHandleDelete() {
     await batchDelete({ids: selectedRowKeys.value},handleSuccess);
   }
   /**
    * 成功回调
    */
  function handleSuccess() {
      (selectedRowKeys.value = []) && reload();
   }
   /**
      * 操作栏
      */
  function getTableAction(record){
       return [
         {
           label: '编辑',
           onClick: handleEdit.bind(null, record),
           auth: 'ftu:ftu_f411_device:edit'
         }
       ]
   }


  /**
   * 下拉操作栏
   */
  function getDropDownAction(record){
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      }
      , {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
          placement: 'topLeft'
        },
        auth: 'ftu:ftu_f411_device:delete'
      }
    ]
  }


</script>

<style lang="less" scoped>
  :deep(.ant-picker),:deep(.ant-input-number){
    width: 100%;
  }
</style>
