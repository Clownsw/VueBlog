<template>
    <div class="app-container">
        <el-row style="display: flex; justify-content: center; align-items: flex-start; margin: 0 auto; width: 950px">
            <el-card class="box-card sort-list-card" style="margin-right: 20px; width: 100%">
                <el-table :data="sorts" style="width: 100%">

                    <el-table-column prop="id" label="分类ID" align="center">
                    </el-table-column>

                    <el-table-column prop="name" label="分类名称" align="center">
                    </el-table-column>

                    <el-table-column prop="order" label="优先级" align="center">
                    </el-table-column>

                    <el-table-column prop="action" label="操作" align="center">
                        <template slot-scope="scope">
                            <el-button type="primary" size="mini" @click="editSort(scope.row)"
                                style="margin-right: 5px">编辑
                            </el-button>

                            <el-popconfirm title="是否继续删除？" @confirm="deleteSort(scope.row.id)">
                                <el-button type="danger" size="mini" slot="reference">删除</el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>

            <el-card class="box-card add-sort-card">
                <div slot="header" class="clearfix">
                    <span>添加分类</span>
                </div>
                <el-form :model="sort" :rules="sortRules" ref="ruleForm" label-width="100px">
                    <el-form-item label="分类名称" prop="name">
                        <el-input v-model="sort.name"></el-input>
                    </el-form-item>

                    <el-form-item label="优先级" prop="order">
                        <el-input v-model="sort.order"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')" size="mini">{{ mode === 0 ? '添加' : '修改' }}
                        </el-button>
                        <el-button v-if="mode !== 0" @click="resetMode" size="mini">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </el-row>
    </div>
</template>

<script>
import sortApi from '@/api/sort'

export default {
    name: 'SortIndex',
    data() {
        return {
            sorts: [],
            sort: {
                order: 0
            },
            sortRules: {
                name: [
                    { required: true, message: '请输入分类名称', trigger: 'blur' },
                    { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
                ],
                order: [
                    { required: true, message: '请输入优先级', trigger: 'blur' },
                ]
            },
            mode: 0, // 0 = 添加, 1 = 修改
        }
    },
    methods: {
        fetchGetSortList() {
            sortApi.getSortList().then(resp => {
                this.sorts = resp.data
            })
        },
        fetchSaveSort() {
            return sortApi.saveSort(this.sort)
        },
        fetchUpdateSortById() {
            return sortApi.updateSortById(this.sort)
        },
        fetchDeleteSortById(id) {
            return sortApi.deleteSortById(id)
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let result;

                    this.sort.order = parseInt(this.sort.order)
                    // 添加
                    if (this.mode === 0) {
                        result = this.fetchSaveSort()
                    }
                    // 修改
                    else {
                        result = this.fetchUpdateSortById()
                    }

                    result.then(resp => {
                        if (resp.code === 200) {
                            this.$message.success(resp.message)
                        }

                        this.fetchGetSortList()
                        this.resetMode()
                    })

                } else {
                    return false;
                }
            });
        },
        deleteSort(id) {
            this.fetchDeleteSortById(id).then(resp => {
                if (resp.code === 200) {
                    this.$message.success(resp.message)
                }

                this.fetchGetSortList()
                this.resetMode()
            })
        },
        editSort(sort) {
            this.mode = 1
            this.sort = JSON.parse(JSON.stringify(sort))
        },
        resetMode() {
            this.sort = {
                order: 0
            }
            this.mode = 0
        }
    },
    created() {
        this.fetchGetSortList()
    }
}
</script>

<style scoped>
.text {
    font-size: 14px;
}

.item {
    margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}

.clearfix:after {
    clear: both
}

.box-card {
    width: 480px;
}
</style>
