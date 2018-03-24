/*global Vue*/
import Router from 'vue-router';
import MainPage from '@/page/MainPage';
import SettingPage from '@/page/SettingPage';

Vue.use(Router)

module.exports = new Router({
    routes: [
        {
            path: '/',
            component: MainPage
        },
        {
            path: '/MainPage',
            name: 'MainPage',
            component: MainPage
        },
        {
            path: '/SettingPage',
            name: 'SettingPage',
            component: SettingPage
        }
    ]
})
