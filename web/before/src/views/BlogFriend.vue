<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <div class="m-friends">

      <a :href="item.href" v-for="item in friends">
        <div class="image">
          <img :src="item.avatar" alt="">
        </div>
        <div class="content">
          <h4>{{ item.name }}</h4>
          <p>{{ item.description }}</p>
        </div>
      </a>

      <a href="#">
        <div class="image">
          <img src="https://cdn.jsdelivr.net/gh/tdrme/tdrme.github.io@master/20210504090204404.png" alt="">
        </div>
        <div class="content">
          <h4>SuiNian's Blog</h4>
          <p>你歪头眯眼一笑 我便沉溺无法逃离</p>
        </div>
      </a>

      <a href="#">
        <div class="image">
          <img src="https://cdn.jsdelivr.net/gh/tdrme/tdrme.github.io@master/20210504090204404.png" alt="">
        </div>
        <div class="content">
          <h4>SuiNian's Blog</h4>
          <p>你歪头眯眼一笑 我便沉溺无法逃离</p>
        </div>
      </a>

      <a href="#">
        <div class="image">
          <img src="https://cdn.jsdelivr.net/gh/tdrme/tdrme.github.io@master/20210504090204404.png" alt="">
        </div>
        <div class="content">
          <h4>SuiNian's Blog</h4>
          <p>你歪头眯眼一笑 我便沉溺无法逃离</p>
        </div>
      </a>

      <a href="#">
        <div class="image">
          <img src="https://cdn.jsdelivr.net/gh/tdrme/tdrme.github.io@master/20210504090204404.png" alt="">
        </div>
        <div class="content">
          <h4>SuiNian's Blog</h4>
          <p>你歪头眯眼一笑 我便沉溺无法逃离</p>
        </div>
      </a>

      <a href="#">
        <div class="image">
          <img src="https://cdn.jsdelivr.net/gh/tdrme/tdrme.github.io@master/20210504090204404.png" alt="">
        </div>
        <div class="content">
          <h4>SuiNian's Blog</h4>
          <p>你歪头眯眼一笑 我便沉溺无法逃离</p>
        </div>
      </a>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";

export default {
  name: "BlogFriend",
  components: {
    Header
  },
  data() {
    return {
      friends: [],
      systemInfo: {},
    }
  },
  methods: {
    getFriends() {
      this.$axios.get("friends").then(resp => {
        this.friends = resp.data.data
      })
    },
  },
  created() {
    this.systemInfo = this.$store.getters.getSystemInfo
    document.title = '友联 - ' + this.systemInfo.title
    this.getFriends()
  }
}
</script>

<style scoped>
.m-friends {
  display: flex;
  flex-flow: row wrap;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

a {
  flex: 1;
  display: inline-block;
  box-sizing: border-box;
  min-width: 250px;
  height: 160px;
  margin: 5px;
  padding: 10px;
  background-color: red;
  border-radius: 10px;
  text-decoration: none;
  overflow: hidden;
}


a img {
  display: block;
  width: 70px;
  height: 70px;
  margin: 0 auto 10px auto;
  border-radius: 50%;
}

a h4, p {
  margin: 0;
  text-align: center;
  color: #fff;
}

a h4 {
  margin-bottom: 5px;
}
</style>
