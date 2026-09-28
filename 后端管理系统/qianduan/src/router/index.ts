import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";
import { useAuthStore } from "@/store";

// 布局组件
const Layout = () => import("@/layout/index.vue");

// 页面组件
const Login = () => import("@/views/login/index.vue");
const Dashboard = () => import("@/views/dashboard/index.vue");
const UserList = () => import("@/views/users/list.vue");
const UserDetail = () => import("@/views/users/detail.vue");
const AttractionList = () => import("@/views/attractions/list.vue");
const AttractionForm = () => import("@/views/attractions/form.vue");
const AttractionDetail = () => import("@/views/attractions/detail.vue");
const OrderList = () => import("@/views/orders/list.vue");
const OrderDetail = () => import("@/views/orders/detail.vue");
const GuideList = () => import("@/views/guides/list.vue");
const GuideDetail = () => import("@/views/guides/detail.vue");
const CommentList = () => import("@/views/comments/list.vue");
const BannerList = () => import("@/views/banners/list.vue");
const TicketList = () => import("@/views/tickets/list.vue");
const TicketForm = () => import("@/views/tickets/form.vue");
const SystemConfig = () => import("@/views/system/config.vue");
const AdminList = () => import("@/views/system/admin.vue");

const routes: RouteRecordRaw[] = [
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: { title: "登录", requiresAuth: false },
  },
  {
    path: "/",
    component: Layout,
    redirect: "/dashboard",
    meta: { requiresAuth: true },
    children: [
      {
        path: "dashboard",
        name: "Dashboard",
        component: Dashboard,
        meta: { title: "数据总览", icon: "Dashboard" },
      },
      {
        path: "users",
        name: "UserList",
        component: UserList,
        meta: { title: "用户管理", icon: "Users" },
      },
      {
        path: "users/:id",
        name: "UserDetail",
        component: UserDetail,
        meta: { title: "用户详情", hidden: true },
      },
      {
        path: "attractions",
        name: "AttractionList",
        component: AttractionList,
        meta: { title: "景点管理", icon: "Map" },
      },
      {
        path: "attractions/create",
        name: "AttractionCreate",
        component: AttractionForm,
        meta: { title: "创建景点", hidden: true },
      },
      {
        path: "attractions/:id",
        name: "AttractionDetail",
        component: AttractionDetail,
        meta: { title: "景点详情", hidden: true },
      },
      {
        path: "attractions/:id/edit",
        name: "AttractionEdit",
        component: AttractionForm,
        meta: { title: "编辑景点", hidden: true },
      },
      {
        path: "orders",
        name: "OrderList",
        component: OrderList,
        meta: { title: "订单管理", icon: "ShoppingCart" },
      },
      {
        path: "orders/:id",
        name: "OrderDetail",
        component: OrderDetail,
        meta: { title: "订单详情", hidden: true },
      },
      {
        path: "guides",
        name: "GuideList",
        component: GuideList,
        meta: { title: "攻略管理", icon: "FileText" },
      },
      {
        path: "guides/:id",
        name: "GuideDetail",
        component: GuideDetail,
        meta: { title: "攻略详情", hidden: true },
      },
      {
        path: "comments",
        name: "CommentList",
        component: CommentList,
        meta: { title: "评论管理", icon: "MessageSquare" },
      },
      {
        path: "banners",
        name: "BannerList",
        component: BannerList,
        meta: { title: "Banner管理", icon: "Image" },
      },
      {
        path: "tickets",
        name: "TicketList",
        component: TicketList,
        meta: { title: "门票管理", icon: "Ticket" },
      },
      {
        path: "tickets/create",
        name: "TicketCreate",
        component: TicketForm,
        meta: { title: "创建门票", hidden: true },
      },
      {
        path: "tickets/:id/edit",
        name: "TicketEdit",
        component: TicketForm,
        meta: { title: "编辑门票", hidden: true },
      },
      {
        path: "system/config",
        name: "SystemConfig",
        component: SystemConfig,
        meta: { title: "系统配置", icon: "Settings" },
      },
      {
        path: "system/admin",
        name: "AdminList",
        component: AdminList,
        meta: { title: "管理员管理", icon: "Shield" },
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/dashboard",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // 设置页面标题
  document.title = `${to.meta.title || "旅游小程序后台"} - 管理系统`;

  // 检查是否需要登录
  if (to.meta.requiresAuth !== false && !authStore.isLoggedIn) {
    next("/login");
  } else if (to.path === "/login" && authStore.isLoggedIn) {
    next("/dashboard");
  } else {
    next();
  }
});

export default router;
