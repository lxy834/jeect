<template>
  <div>
    <!--引用表格-->
   <BasicTable @register="registerTable" :rowSelection="rowSelection">
     <!--插槽:table标题-->
      <template #tableTitle>
          <a-button type="primary" v-auth="'ftu:ftu_device:add'"  @click="handleAdd" preIcon="ant-design:plus-outlined"> 新增</a-button>
          <a-button  type="primary" v-auth="'ftu:ftu_device:exportXls'"  preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
          <j-upload-button  type="primary" v-auth="'ftu:ftu_device:importExcel'"  preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
              <template #overlay>
                <a-menu>
                  <a-menu-item key="1" @click="batchHandleDelete">
                    <Icon icon="ant-design:delete-outlined"></Icon>
                    删除
                  </a-menu-item>
                </a-menu>
              </template>
              <a-button v-auth="'ftu:ftu_device:deleteBatch'">批量操作
                <Icon icon="mdi:chevron-down"></Icon>
              </a-button>
        </a-dropdown>
        <!-- 高级查询 -->
        <super-query :config="superQueryConfig" @search="handleSuperQuery" />
      </template>
       <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)"/>
      </template>
      <!--字段回显插槽-->
      <template v-slot:bodyCell="{ column, record, index, text }">
      </template>
    </BasicTable>
    <!-- 表单区域 -->
    <FtuDeviceModal @register="registerModal" @success="handleSuccess"></FtuDeviceModal>

    <el-dialog
      v-model="state.dialog"
      title="设备绑定"
      width="500"
      center
      draggable
    >
      <div style="padding:10px">
        <el-button type="primary" size="mini" @click="addNewColumn">添加列</el-button>
        <!-- 列配置区域 -->
        <el-select
          v-model="state.newColumnType"
          placeholder="选择列类型"
          style="width: 180px; margin-left: 10px"
        >
          <el-option label="文本" value="text"></el-option>
          <el-option label="数字" value="number"></el-option>
          <el-option label="日期" value="date"></el-option>
        </el-select>
        <el-input
          v-model="state.newColumnName"
          placeholder="输入列标题"
          style="width: 180px; margin-left: 10px"
        ></el-input>
      </div>

      <el-table :data="state.tableData" border style="margin:10px" ref="tb">
        <!-- 动态生成列 -->
        <el-table-column
          v-for="(col, index) in state.columns"
          :key="index"
          :prop="col.prop"
          :label="col.label"
          :width="col.width || 120"
        >
          <!-- 不同类型列的渲染 -->
          <template #default="scope">
            <template v-if="col.type === 'date'">
              {{ scope.row[col.prop] ? formatDate(scope.row[col.prop]) : '' }}
            </template>
            <template v-else>
              {{ scope.row[col.prop] }}
            </template>
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button
              size="mini"
              type="danger"
              @click="deleteRow(scope.$index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script lang="ts" name="ftu-ftuDevice" setup>
  import {ref, reactive, computed, unref} from 'vue';
  import {BasicTable, useTable, TableAction} from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage'
  import {useModal} from '/@/components/Modal';
  import FtuDeviceModal from './components/FtuDeviceModal.vue'
  import {columns, searchFormSchema, superQuerySchema} from './FtuDevice.data';
  import {list, deleteOne, batchDelete, getImportUrl,getExportUrl} from './FtuDevice.api';
  import {downloadFile} from '/@/utils/common/renderUtils';
  import { useUserStore } from '/@/store/modules/user';
  const queryParam = reactive<any>({});
  const checkedKeys = ref<Array<string | number>>([]);
  const userStore = useUserStore();
  //注册model
  const [registerModal, {openModal}] = useModal();
   //注册table数据
  const { prefixCls,tableContext,onExportXls,onImportXls } = useListPage({
      tableProps:{
           title: 'FTU终端',
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
                   ['commTime', ['commTime_begin', 'commTime_end'], 'YYYY-MM-DD HH:mm:ss'],
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
            name:"FTU终端",
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

  const state = reactive({
    dialog:false,
    tableData:[],
    columns: [], // 动态列配置
    newColumnType: 'text', // 新列类型
    newColumnName: '', // 新列标题
    propCounter: 1, // 属性计数器，用于生成唯一prop
  })

  /**
   * 添加新列
   */
  function addNewColumn() {
    if (!state.newColumnName.trim()) {
      ElMessage.warning('请输入列标题');
      return;
    }

    // 生成唯一prop（避免重复）
    const newProp = `custom_col_${state.propCounter++}`;

    // 添加列配置
    state.columns.push({
      label: state.newColumnName,
      prop: newProp,
      type: state.newColumnType,
    });

    // 为现有数据添加新列默认值
    state.tableData.forEach(row => {
      switch(state.newColumnType) {
        case 'text':
          row[newProp] = '';
          break;
        case 'number':
          row[newProp] = 0;
          break;
        case 'date':
          row[newProp] = new Date();
          break;
      }
    });

    // 清空输入
    state.newColumnName = '';
  }

  /**
   * 删除行数据
   */
  function deleteRow(index: number) {
    state.tableData.splice(index, 1);
  }

  /**
   * 日期格式化
   */
  function formatDate(date: Date | string) {
    if (typeof date === 'string') date = new Date(date);
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
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
           auth: 'ftu:ftu_device:edit'
         }
       ]
   }

   function test(){
     state.dialog = true
   }

  /**
   * 下拉操作栏
   */
  function getDropDownAction(record){
    return [
      {
        label: '详情',
        onClick: handleDetail.bind(null, record),
      }, {
        label: '测试',
        onClick: test,
      }
      , {
        label: '删除',
        popConfirm: {
          title: '是否确认删除',
          confirm: handleDelete.bind(null, record),
          placement: 'topLeft'
        },
        auth: 'ftu:ftu_device:delete'
      }
    ]
  }


</script>

<style lang="less" scoped>
  :deep(.ant-picker),:deep(.ant-input-number){
    width: 100%;
  }
</style>
