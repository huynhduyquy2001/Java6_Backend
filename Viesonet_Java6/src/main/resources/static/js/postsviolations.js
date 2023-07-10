function list(id) {
	$.ajax({
		url: "/admin/postsviolations/list/" + id, // Thay đổi đường dẫn tới máy chủ của bạn
		method: 'GET', // Hoặc 'GET' tùy vào phương thức gửi dữ liệu
		success: function(data) {

			var tr = document.getElementById("listTable")
			tr.innerHTML = "";

			data.forEach(function(item) {
				tr.innerHTML += `<tr>
								     <td>${item[0]}</td>
									 <td class="text-center">${item[1]}</td>
								 </tr>`;
			});

			$("#exampleModal").modal("show");
		},
		error: function(xhr, status, error) {
			// Xử lý lỗi (nếu có)
			console.error('Lỗi khi gửi dữ liệu:', error);
		}
	});
}

function removeViolations(id) {
	Swal.fire({
		text: 'Bạn có chắc muốn đổi vai trò không?',
		icon: 'warning',
		confirmButtonText: 'Có, chắc chắn',
		showCancelButton: true,
		confirmButtonColor: '#159b59',
		cancelButtonColor: '#d33'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: "/admin/postsviolations/removeViolations/" + id, // Thay đổi đường dẫn tới máy chủ của bạn
				method: 'GET', // Hoặc 'GET' tùy vào phương thức gửi dữ liệu
				success: function(data) {
					console.log(data.length)
					var row = document.getElementById("postRow")
					row.innerHTML = ""
					data.forEach(function(item) {
						//Chuyển định dạng ngày tháng năm
						let date = new Date(item.postDate);
						let day = date.getUTCDate().toString().padStart(2, '0');
						let month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
						let year = date.getUTCFullYear().toString();
						let datePosts = `${day}-${month}-${year}`;


						var card1 = `<a href="#" class="d-flex align-items-center"> <img src="/images/${item.avatar}" class="rounded-circle"
			              style="object-fit: cover;" width="50" height="50">
			            <div class="ms-3">
			              <h4 class="card-title">${item.username}</h4>
			              <p class="card-subtitle mb-0">${datePosts}</p>
			            </div>
			          </a>
			          <div class="ms-auto">
			            <div class="dropdown">
			              <a href="#" class="link" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false"> 
			                   <i class="fa-regular fa-ellipsis-vertical fa-xl"></i>
			              </a>
			              <ul style="background: whitesmoke;" class="dropdown-menu" aria-labelledby="dropdownMenuButton">
			                <li><a class="dropdown-item" href="#" onclick="list('${item.postId}')">
			                    Xem danh sách lý do tố cáo</a></li>
			                <li><a class="dropdown-item" href="#" onclick="removeViolations('${item.postId}')">Gỡ vi phạm</a></li>
			              </ul>
			            </div>
			          </div>`


						var imgs = item.allImages;
						var imageUrls = imgs.split(", ");
						var card2 = `<div style="height: 230px; width: 250px; overflow: hidden;" id="carouselExampleFade-${item.postId}"
			            class="mt-4 carousel slide carousel-fade carousel-dark">
			            <div class="carousel-inner">`;
						imageUrls.forEach(function(img, index) {
							var activeClass = index === 0 ? "active" : "";
							card2 += `
			              <div class="carousel-item ${activeClass}">
			                <img src="/images/${img}" width="85%" style="object-fit: cover;">
			              </div>`})
							if (imageUrls.length > 1) {
								card2 += ` </div>
					          </div>
					          <div>
				            <button style="height: 50px; margin-top: 200px;" class="carousel-control-prev" type="button"
				              data-bs-target="#carouselExampleFade-${item.postId}" data-bs-slide="prev">
				              <span class="carousel-control-prev-icon" aria-hidden="true"></span>
				            </button>
				            <button style="height: 50px; margin-top: 200px;" class="carousel-control-next" type="button"
				              data-bs-target="#carouselExampleFade-${item.postId}" data-bs-slide="next">
				              <span class="carousel-control-next-icon" aria-hidden="true"></span>
				            </button>
				          </div>`
						}

						var style = ``;
						if (imageUrls == null) {
							style += ``
						} else {
							style += `style="max-width: 350px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;"`
						}
						var card3 = `<div class="mt-4">
			          <p class="fs-4" data-bs-toggle="tooltip" data-bs-placement="bottom" title="${item.content}"
			            ${style}>${item.content}</p>
			        </div>
			        <div class="d-flex align-items-center">
			          <div>
			             <i class="fa-regular fa-thumbs-up"></i>  &nbsp; <span>${item.likeCount}</span>
			          </div>
			          &nbsp;&nbsp;&nbsp;&nbsp;
			          <div>
			            <i class="fa-regular fa-comment"></i>&nbsp; <span>${item.commentCount}</span>
			          </div>
			        </div>`

						row.innerHTML += `<div class="col-4">
										    <div class="card">
										      <div class="card-body">
										        <div class="d-flex align-items-center">
										         ${card1}
										        </div>
										        <center class="carousel-dark">
										          ${card2}
										        </center>
										          ${card3}
										      </div>
										    </div>
										  </div>`

					})
					Swal.fire({
						position: 'top',
						icon: 'success',
						text: 'Gỡ vi phạm thành công',
						showConfirmButton: false,
						timer: 1800
					})
				},
				error: function(xhr, status, error) {
					Swal.fire({
						position: 'top',
						icon: 'error',
						text: 'Gỡ vi phạm thất bại!',
						showConfirmButton: false,
						timer: 1800
					})
					// Xử lý lỗi (nếu có)
					console.error('Lỗi khi gửi dữ liệu:', error);
				}
			});
		}
	})
}