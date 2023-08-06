setTimeout(function() {
	const currentURL = window.location.href;
	const menuLinks = document.querySelectorAll("#sidebarnav .sidebar-link");
	var check = false;
	for (let i = 0; i < menuLinks.length; i++) {
		const link = menuLinks[i];
		const linkURL = link.getAttribute("href");

		if (currentURL.includes(linkURL) && linkURL !== "/") {
			link.classList.add("active");
			check = true;
			break;
			 // Thoát khỏi vòng lặp sau khi thêm lớp "active"
		}
	}
	if(check==false){
		menuLinks[0].classList.add("active");
	}
}, 100);
