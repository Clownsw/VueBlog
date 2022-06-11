<template>
  <div>
    <Header :welcome="systemInfo.welcome"></Header>
    <div class="m-friends">

      <a :href="item.href" target="_blank" v-for="item in friends">
        <div class="image">
          <img :src="item.avatar" alt="">
        </div>
        <div class="content">
          <h4>{{ item.name }}</h4>
          <p>{{ item.description }}</p>
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
      systemInfo: this.$store.getters.getSystemInfo,
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
  box-shadow: 5px 5px 12px #8aa1f9;
  box-sizing: border-box;
  min-width: 250px;
  height: 160px;
  margin: 10px;
  padding: 10px;
  background-color: #adb4f5;
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
