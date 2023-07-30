  angular.module('myApp', ['pascalprecht.translate'])
    .config(function ($translateProvider) {
        $translateProvider.translations('vie', {
			//menuLeft
            'home': 'Trang chủ',
            'chung': 'Chung',
            'TK': 'Tìm kiếm',
            'GY': 'Gợi ý',
            'QL': 'Quản Lý',
            'TN': 'Tin nhắn',
            'ND': 'Người dùng',
            'BV': 'Bài viết',
            'BVVP': 'Bài viết vi phạm',
            //headeer
            'DMK': 'Đổi mật khẩu',
            'DK': 'Thông tin điều khoản',
            'NC':'Nâng cấp',
            'DX':'Đăng xuất',
            
        })
        $translateProvider.translations('en', {
            'home': 'Home',
            'chung': 'Shared',
            'TK': 'Search	',
            'GY': 'Suggest',
            'QL': 'Manage',
            'TN': 'Message',
            'ND': 'User',
            'BV': 'Posts',
            'BVVP': 'Infringing post',
            //headeer
            'DMK': 'Change Password',
            'DK': 'Terms Information',
            'NC':'Upgrade',
            'DX':'Log out',
         });

        // Set the default language
        $translateProvider.preferredLanguage('vie');
    })