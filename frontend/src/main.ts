import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios' // 1. Импортируем сам axios

import App from './App.vue'
import router from './router'

// 2. Настраиваем базовый URL глобально
// Если проект запущен на компе в режиме разработки (npm run dev),
// то baseUrl будет 'http://localhost:8080'.
// Если мы собираем проект для сервера (npm run build),
// то baseUrl будет пустой строкой '', и запросы пойдут по относительному пути!
axios.defaults.baseURL = import.meta.env.MODE === 'production'
    ? ''
    : 'http://localhost:8080'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')