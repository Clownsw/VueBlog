<template>
    <div class="app-container">
        <el-form :inline="true">
            <el-form-item>
                <el-button type="primary" @click="addFriendDialog">新增</el-button>
            </el-form-item>

            <el-form-item>
                <el-popconfirm title="这是确定批量删除吗?" @confirm="deleteFriends">
                    <el-button type="danger" slot="reference" :disabled="deleteStatus">批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%" border stripe
            @selection-change="handleSelectionChange" class="data-table">
            <el-table-column type="selection" align="center">
            </el-table-column>

            <el-table-column prop="id" label="ID" width="50" align="center">
            </el-table-column>

            <el-table-column prop="name" label="友链名称" width="200" show-overflow-tooltip align="center">
            </el-table-column>

            <el-table-column prop="description" label="友链描述" width="350" show-overflow-tooltip align="center">
            </el-table-column>

            <el-table-column prop="href" label="友链地址" show-overflow-tooltip align="center">
            </el-table-column>

            <el-table-column prop="avatar" label="友链头像" show-overflow-tooltip align="center">
            </el-table-column>

            <el-table-column prop="action" label="操作" width="200" show-overflow-tooltip align="center">
                <template slot-scope="scope">
                    <el-button type="info" @click="editFriend(scope.row.id)">编辑</el-button>

                    <el-popconfirm confirm-button-text='删除' cancel-button-text='取消' icon="el-icon-info" icon-color="red"
                        title="您确定要删除该用户吗？" style="margin-left: 5px" @confirm="deleteFriends(scope.row.id)">
                        <el-button type="danger" slot="reference">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="提示" :visible.sync="dialogStatus" top="8vh" width="30%" :before-close="handleClose">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="友链名称" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>

                <el-form-item label="友链描述" prop="description">
                    <el-input v-model="ruleForm.description"></el-input>
                </el-form-item>

                <el-form-item label="友链链接" prop="href">
                    <el-input v-model="ruleForm.href"></el-input>
                </el-form-item>

                <el-form-item label="友链头像" prop="avatar">
                    <el-input v-model="ruleForm.avatar"></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">{{ dialogTitle }}</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <div class="limit">
            <el-pagination background layout="prev, pager, next" :page-size="size" :currnet-page="current"
                :total="total" @current-change=getFriends>
            </el-pagination>
        </div>
    </div>
</template>

<script>
import friendApi from '@/api/friend'

export default {
    name: 'OtherFriend',
    data() {
        return {
            searchName: '',
            deleteStatus: true,
            dialogStatus: false,
            dialogTitle: '',
            isAdd: true,
            total: 0,
            size: 0,
            current: 1,
            pages: 0,
            tableData: [],
            multipleSelection: [],
            ruleForm: {
                name: '',
                description: '',
                href: '',
                avatar: '',
            },
            rules: {
                name: [
                    { required: true, message: '请输入友链名称', trigger: 'blur' },
                ],
                description: [
                    { required: true, message: '请输入友链描述', trigger: 'blur' },
                ],
                href: [
                    { required: true, message: '请输入友链地址', trigger: 'blur' },
                ],
                avatar: [
                    { required: true, message: '请输入友链头像', trigger: 'blur' },
                ],
            }
        }
    }
    ,
    methods: {
        fetchGetFriendList(currentPage) {
            return friendApi.getFriendList(currentPage)
        },
        fetchSaveFriend() {
            return friendApi.saveFriend(this.ruleForm)
        },
        fetchUpdateFriendById() {
            return friendApi.updateFriendById(this.ruleForm)
        },
        fetchBatchDeleteByIds(ids) {
            return friendApi.batchDeleteByIds(ids)
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            this.deleteStatus = this.multipleSelection.length <= 0;
        },
        handleClose(done) {
            this.$confirm('确认关闭？')
                .then(_ => {
                    this.dialogStatus = false
                    done();
                })
                .catch(_ => {
                });
        },
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let resp = this.isAdd ? this.fetchSaveFriend() : this.fetchUpdateFriendById();

                    resp.then(resp => {
                        this.$message.success(resp.message)
                        this.dialogStatus = false
                        this.getFriends(this.current)
                    })
                } else {
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        addFriendDialog() {
            this.dialogStatus = true
            this.isAdd = true
            this.dialogTitle = this.isAdd ? '添加' : '修改'
        },
        editFriend(id) {
            this.dialogStatus = true
            this.isAdd = false
            this.dialogTitle = this.isAdd ? '添加' : '修改'

            for (let i = 0; i < this.tableData.length; i++) {
                if (this.tableData[i].id === id) {
                    this.ruleForm = JSON.parse(JSON.stringify(this.tableData[i]))
                    break
                }
            }
        },
        deleteFriends(id) {
            let ids = []
            if (id === undefined) {
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    ids.push(this.multipleSelection[i].id)
                }
            } else {
                ids.push(id)
            }

            this.fetchBatchDeleteByIds(ids).then(resp => {
                this.$message.success(resp.message)
                this.getFriends(this.current)
            })
        },
        getFriends(currentPage) {
            this.fetchGetFriendList(currentPage).then(resp => {
                if (resp.code !== 200) {
                    this.$message.error(resp.message)
                    return
                }

                resp = resp.data

                this.total = resp.totalCount
                this.size = resp.pageSize
                this.pages = resp.pageCount
                this.current = resp.currentPage
                this.tableData = resp.dataList
            })
        }
    },
    created() {
        this.getFriends(1)
    }
}
</script>

<style scoped>
.limit {
    display: flex;
    justify-content: center;
    margin-top: 8px;
}
</style>