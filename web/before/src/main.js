import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

import Prism from 'prismjs';
import hljs from 'highlight.js';
import './permisson'

VueMarkdownEditor.use(vuepressTheme, {
    Prism,
});

VMdPreview.use(githubTheme, {
    Hljs: hljs,
});

import './axios'

let app = createApp(App)
app.use(store)
    .use(router)
    .use(VueMarkdownEditor)
    .use(ElementPlus)
    .use(VMdPreview)
    .mount('#app')
	
app.config.productionTip = false
app.config.globalProperties.$axios = axios