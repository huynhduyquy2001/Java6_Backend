package com.viesonet.admin;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.dao.UsersDao;
import com.viesonet.entity.*;
import com.viesonet.report.*;

import jakarta.transaction.Transactional;

@Transactional
@RestController
public class Report {
	
	@Autowired
	UsersDao userDAO;
	
	@Autowired
	sp_FilterPostLike filterPostsLike;
	
	@Autowired
	sp_ListYear EXEC1;
	
	@Autowired
	sp_TopPostLike topLike;
	
	@Autowired
	sp_ListAcc listCountAcc;
	
	@Autowired
	sp_ViolationsPosts EXEC;
	
	@Autowired
	sp_NumberReport EXEC2;
	
	@Autowired
	sp_TotalPosts totalPosts;
	
	@Autowired
	sp_Last7DaySumAccounts last7DaySumAccounts;
	
	
	@Autowired
	sp_SumAccountsByDay sumAccountsByDay;
	
	
	//Load dữ liệu khi mở lên
	@GetMapping("/admin/report")
	public String thongKe(Model m) {
		//Tìm người dùng vai trò là admin
		m.addAttribute("acc", userDAO.findByuserId("UI001"));
		
		//Thực hiện gọi stored procedure
		List<ViolationsPosts> rs = EXEC.executeReportViolationPosts(LocalDate.now().getYear());
		List<NumberReport> rs2 = EXEC2.executeNumberReport(LocalDate.now().getYear());
		
		//Lấy năm
		List<ListYear> list = EXEC1.executeListYear();
		
		//Truyền dữ liệu
		m.addAttribute("yearNow", LocalDate.now().getYear());
		m.addAttribute("listYear", list);
		m.addAttribute("soluotBaoCao", rs);
		m.addAttribute("soBaiViet", rs2);
		
		// Lấy danh sách các người dùng
		List<Users> usersList = userDAO.findAll();
		
		//Tạo danh sách nhóm tuổi
		List<Users> age18to25 = new ArrayList<>();
        List<Users> from25to35 = new ArrayList<>();
        List<Users> from35andAbove = new ArrayList<>();
        
        //Chạy vòng lặp để thêm vào từng nhóm
        for (Users user : usersList) {
            int age = getAge(convertToLocalDate(user.getBirthday()));
            String category = getCategory(age);

            if (category.equals("Từ 18 đến 25 tuổi")) {
                age18to25.add(user);
            } else if (category.equals("Từ 25 đến 35 tuổi")) {
                from25to35.add(user);
            } else if (category.equals("35 tuổi trở lên")) {
                from35andAbove.add(user);
            }
        }
        
        //Đếm tổng số lượng tài khoản
        double tongSo = usersList.size();
        
        //Tính phần trăm độ tuổi
        double nhom18den25 = (age18to25.size() / tongSo )* 100;
        double nhom25den35 = (from25to35.size() / tongSo )* 100;
        double tu35troLen = (from35andAbove.size() / tongSo )* 100;
        
        //Đưa dữ liệu qua js để hiển thị
        m.addAttribute("nhom1", Math.round(nhom18den25 * 10 + 0.05) / 10.0); // Math.round() để làm tròn số thập phân
        m.addAttribute("nhom2", Math.round(nhom25den35 * 10 + 0.05) / 10.0);
        m.addAttribute("nhom3", Math.round(tu35troLen * 10 + 0.05) / 10.0);
        
        //Thống kê số accounts mới tham gia trong 7 ngày qua
        List<ListAcc> listAc = listCountAcc.executeListAcc();
        
        //Truyền dữ liệu
        m.addAttribute("listAcc", listAc);
        
        //Thống kê top 5 bài viết được yêu thích
        List<TopPostLike> listTopLike = topLike.executeTopLike();
        
        //Truyền dữ liệu
        m.addAttribute("topLike", listTopLike);
        
        //Thống kê tài khoản có nhiều bài viết nhất
        List<TotalPosts> tPosts = totalPosts.executeTotalPosts();
        //Truyền dữ liệu
        m.addAttribute("TotalPosts", tPosts); 
		return "/admin/report";
	}
	
	
	//Phương thức lọc theo năm
	@ResponseBody
	@RequestMapping("/admin/report/filterYear/{year}")
	public ReportAndViolations filterYear(@PathVariable int year){
		//Nhận tham số và thực hiện theo năm đã chọn
		List<ViolationsPosts> rs = EXEC.executeReportViolationPosts(year);
		List<NumberReport> rs2 = EXEC2.executeNumberReport(year);
		
		//Trả về list chứa 2 danh sách
		return new ReportAndViolations(rs,rs2);
	}
	
	//Phương thức xem chi tiết bài viết
	@ResponseBody
	@RequestMapping("/admin/report/detail/{postId}")
	public TopPostLike detailPosts(Model m, @PathVariable int postId) {
		return filterPostsLike.executeFilterPostLike(postId);
	}
	
	 //Phương thức để tính độ tuổi
	 private static int getAge(LocalDate birthday) {
		 //Lấy ngày tháng năm hiện tại
	        LocalDate currentDate = LocalDate.now();
	        
	     //Sử dụng thư viện period để tính độ tuổi
	        Period period = Period.between(birthday, currentDate);
	        
	        return period.getYears();
	    }
	 
	 //Phương thức phân loại nhóm tuổi
	 private static String getCategory(int age) {
	        if (age <= 25) {
	            return "Từ 18 đến 25 tuổi";
	        } else if (age > 25 && age <=35) {
	            return "Từ 25 đến 35 tuổi";
	        } else {
	            return "35 tuổi trở lên";
	        }
	    }
    //Phương thức chuyển kiểu Date sang LocalDate
    private static LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
