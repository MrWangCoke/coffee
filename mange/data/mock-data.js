window.MockData = {
  coffeeCategories: ['美式咖啡', '拿铁咖啡', '卡布奇诺', '摩卡咖啡', '焦糖玛奇朵', '冷萃咖啡'],

  recentOrders: [
    { id: '202604280001', customer: '李先生', product: '杨枝甘露 (大杯)', amount: '36.00', status: '待接受', badge: 'badge-blue', time: '04-28 09:58' },
    { id: '202604280002', customer: '张女士', product: '草莓蛋糕', amount: '128.00', status: '已完成', badge: 'badge-green', time: '04-28 09:45' },
    { id: '202604280003', customer: '陈先生', product: '超级披萨套餐', amount: '88.00', status: '待付款', badge: 'badge-orange', time: '04-28 09:32' },
    { id: '202604280004', customer: '王女士', product: '经典牛肉汉堡', amount: '42.00', status: '备货中', badge: 'badge-gray', time: '04-28 09:21' },
  ],

  todos: [
    { icon: 'fa-file-invoice', label: '待付款', count: 32 },
    { icon: 'fa-truck-fast', label: '待发货', count: 6 },
    { icon: 'fa-message', label: '评价回复', count: 18 },
    { icon: 'fa-rotate-left', label: '售后申请', count: 2 },
    { icon: 'fa-ticket', label: '核销记录', count: 1 },
  ],

  previewProducts: [
    { id: 1, name: '冰美式', category: '美式咖啡', price: 22, stock: 88, image_url: 'https://images.unsplash.com/photo-1517701604599-bb29b565090c?auto=format&fit=crop&w=600&q=80' },
    { id: 2, name: '燕麦拿铁', category: '拿铁咖啡', price: 32, stock: 46, image_url: 'https://images.unsplash.com/photo-1570968915860-54d5c301fa9f?auto=format&fit=crop&w=600&q=80' },
    { id: 3, name: '焦糖玛奇朵', category: '焦糖玛奇朵', price: 35, stock: 34, image_url: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=600&q=80' },
    { id: 4, name: '冷萃咖啡', category: '冷萃咖啡', price: 29, stock: 52, image_url: 'https://images.unsplash.com/photo-1461023058943-07fcbe16d735?auto=format&fit=crop&w=600&q=80' },
  ],
};
