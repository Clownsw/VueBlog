import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import 'viewerjs/dist/viewer.css'
import VueViewer from 'v-viewer'
import APlayer from '@moefe/vue-aplayer';

import './axios'
import './interceptor'

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(mavonEditor)
Vue.use(VueViewer)
Vue.use(APlayer, {
    defaultCover: 'https://github.com/u3u.png',
    productionTip: true,
});
Vue.prototype.$axios = axios

new Vue({
    router,
    store,
    render: function (h) {
        return h(App)
    }
}).$mount('#app')
