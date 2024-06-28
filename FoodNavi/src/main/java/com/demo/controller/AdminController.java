package com.demo.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.demo.config.PathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.Admin;
import com.demo.domain.Board;
import com.demo.domain.Comments;
import com.demo.domain.ExerciseOption;
import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.domain.FoodIngredient;
import com.demo.domain.Ingredient;
import com.demo.domain.Users;
import com.demo.dto.BoardScanVo;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.FoodVo;
import com.demo.dto.UserScanVo;
import com.demo.persistence.ExerciseOptionRepository;
import com.demo.service.AdminFoodDetailService;
import com.demo.service.AdminFoodService;
import com.demo.service.AdminService;
import com.demo.service.AdminUsersService;
import com.demo.service.BoardCommentsService;
import com.demo.service.BoardService;
import com.demo.service.DataInOutService;
import com.demo.service.ExerciseOptionService;
import com.demo.service.FoodIngredientService;
import com.demo.service.IngredientService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@SessionAttributes("adminUser")
@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminUsersService usersService;
	@Autowired
	private AdminFoodDetailService foodDetailService;
	@Autowired
	private AdminFoodService foodService;
	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardCommentsService boardCommentsService;
	@Autowired
	private IngredientService ingredientService;
	@Autowired
	private FoodIngredientService foodIngredientService;
	@Autowired
	private ExerciseOptionRepository exerciseOptionRepository;
	@Autowired
	private ExerciseOptionService exerciseOptionService;
	@Autowired
	private DataInOutService dataInOutService;

	@GetMapping("/admin_login_form")
	public String admin_login_form() {
		return "/admin/login";
	}

	// 어드민계정 로그인기능
	// 1. 로그인 화면을 어드민계정 로그인 페이지를 따로 만들것인지 에따라 Mapping 과 리턴할 페이지값 수정
	// 2. 패스워드가 맞지않을시, 아이디가 존재하지않을시, 액션에 따라 각각 리턴할부분 수정
	@PostMapping("/admin_login")
	public String admin_login(Admin vo, HttpSession session) {

		int result = adminService.adminCheck(vo);
		if (result == 1) {
			Admin admin = adminService.getAdmin(vo.getAdminid());
			session.setAttribute("adminUser", admin);
			return "redirect:admin_main";
		} else if (result == -1) {
			return "admin/login";
		} else if (result == 0) {
			return "admin/login";
		} else {
			return "admin/login";
		}
	}

	// 어드민 로그아웃 기능.. 세션에서 status 삭제
	@GetMapping("/admin_logout")
	public String adminLogout(SessionStatus status) {
		status.setComplete();
		return "admin/login";
	}

	@GetMapping("admin_main")
	public String admin_main(HttpSession session, HttpServletRequest request, Model model) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		model.addAttribute("admin", admin);
		return "admin/main";
	}

	// 회원리스트
	// 1. mapping 과 return값 수정
	// 2. 검색시 사용될 name 값 key 와 다를시 수정
	@GetMapping("admin_user_list")
	public String userList(Model model, HttpServletRequest request,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "8") int size,
						   @RequestParam(value = "sortBy", defaultValue = "useq") String sortBy,
						   @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
						   @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
						   @RequestParam(value = "searchField", defaultValue = "name") String searchField,
						   @RequestParam(value = "searchWord", defaultValue = "") String searchWord, UserScanVo userScanVo,
						   HttpSession session) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			userScanVo.setSearchField(searchField);
			userScanVo.setSearchWord(searchWord);
			userScanVo.setSortBy(sortBy);
			userScanVo.setSortDirection(sortDirection);
			userScanVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			userScanVo = (UserScanVo) session.getAttribute("userScanVo");
		}
		Page<Users> userData = usersService.getUsersList(userScanVo, page, size);
		userScanVo.setPageInfo(userData);
		userScanVo.setUserList(userData.getContent());
		session.setAttribute("userScanVo", userScanVo);
		model.addAttribute("userScanVo", userScanVo);
		model.addAttribute("userList", userScanVo.getUserList());
		model.addAttribute("pageInfo", userScanVo.getPageInfo());

		return "admin/userList";
	}

	@GetMapping("admin_user_detail")
	public String userInfo(@RequestParam(value = "useq") int useq, Model model, HttpSession session,
						   HttpServletRequest request) {
		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Users user = usersService.getUserByUseq(useq);
		model.addAttribute("user", user);
		UserScanVo userScanVo = (UserScanVo) session.getAttribute("userScanVo");
		model.addAttribute("userScanVo", userScanVo);
		model.addAttribute("userList", userScanVo.getUserList());
		model.addAttribute("pageInfo", userScanVo.getPageInfo());
		return "admin/userDetail";
	}
	
	@ResponseBody
	@PostMapping("admin_user_able")
	public Map<String, Integer> admin_user_able(HttpSession session, @RequestParam("useq") int useq) {
		Map<String, Integer> result = new HashMap<>();
		int status = -1;
		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");
		// 세션에 로그인 정보가 없는 경우
		if (admin != null) {
			Users user = usersService.getUserByUseq(useq);
			if (user.getUseyn().equals("y")) {
				user.setUseyn("n");
				status = 0;
			} else {
				user.setUseyn("y");
				status = 1;
			}
			usersService.updateUser(user);
		}
		result.put("status", status);
		
		return result;
	}

	// 식품 리스트
	// 1. mapping 과 return값 수정
	// 2. 검색시 사용될 name 값 key 와 다를시 수정
	@GetMapping("/admin_food_list")
	public String adminFoodList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
								@RequestParam(value = "size", defaultValue = "8") int size,
								@RequestParam(value = "sortBy", defaultValue = "fseq") String sortBy,
								@RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
								@RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
								@RequestParam(value = "searchField", defaultValue = "name") String searchField,
								@RequestParam(value = "searchWord", defaultValue = "") String searchWord, FoodRecommendVo foodScanVo,
								HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);
			foodScanVo.setSortBy(sortBy);
			foodScanVo.setSortDirection(sortDirection);
			foodScanVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			foodScanVo = (FoodRecommendVo) session.getAttribute("foodScanVo");
		}
		Page<Food> foodData = foodService.getFoodList(foodScanVo, page, size);
		List<Food> foodList = foodData.getContent();
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("number", page);
		pageInfo.put("size", size);
		pageInfo.put("totalPages", (foodList.size()+size-1)/size);
		foodScanVo.setPageInfo(pageInfo);
		foodScanVo.setFoodList(foodData.getContent());
		session.setAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodScanVo", foodScanVo);
		List<FoodVo> foodRecommendList = new ArrayList<>();
		for (Food food : foodList) {
			FoodVo foodVo = new FoodVo(food);
			foodRecommendList.add(foodVo);
		}
		foodScanVo.setFoodRecommendList(foodRecommendList);
		model.addAttribute("foodList", foodScanVo.getFoodRecommendList());
		model.addAttribute("pageInfo", foodData);
		return "admin/foodList";

	}


	// 식품정보 상세보기
	@GetMapping("admin_food_detail")
	public String adminFoodInfo(Model model, @RequestParam(value = "fseq") int fseq, HttpSession session,
								HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fseq);
		List<FoodIngredient> foodIngredients = foodIngredientService.getFoodIngredientListByFood(fseq);
		model.addAttribute("foodIngredientsList", foodIngredients);
		int minVeganValue = 5;
		for (FoodIngredient fi : foodIngredients) {
			if (fi.getIngredient().getVeganValue() < minVeganValue)
				minVeganValue = fi.getIngredient().getVeganValue();
		}
		String vegan = "";
		if (minVeganValue == 0) {
			vegan = "해당 없음";
		} else if (minVeganValue == 1) {
			vegan = "폴로-페스코 가능";
		} else if (minVeganValue == 2) {
			vegan = "페스코 가능";
		} else if (minVeganValue == 3) {
			vegan = "락토-오보 가능";
		} else if (minVeganValue == 4) {
			vegan = "락토 가능";
		} else {
			vegan = "비건 가능";
		}
		FoodVo foodVo = new FoodVo(food);
		model.addAttribute("vegan", vegan);
		model.addAttribute("foodVo", foodVo);
		FoodRecommendVo foodScanVo = (FoodRecommendVo) session.getAttribute("foodScanVo");
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodList", foodScanVo.getFoodList());
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());

		return "admin/foodDetail";
	}

	// 식품 등록화면 출력
	@GetMapping("admin_food_insert_form")
	public String adminFoodInsertForm() {
		return "admin/foodInsert";
	}

	// 식품 정보 등록 처리
	// 1. 추가된 입력 정보를 처리하여 데이터베이스에 넣는 작업 필요
	@Transactional
	@PostMapping("admin_food_insert")
	public String adminFoodWriteAction(RedirectAttributes re, HttpSession session, HttpServletRequest request,
									   @RequestParam(value = "name") String name, @RequestParam(value = "img") MultipartFile uploadFile,
									   @RequestParam(value = "ingredient") String[] ingredient,
									   @RequestParam(value = "quantity") int [] quantity,
									   @RequestParam(value = "foodType") String foodType,
									   @RequestParam(value = "n") int n) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}
		
		Food food = new Food();
		food.setName(name);
		if (!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String saveName = uuid + "_" + fileName;
			food.setImg(saveName);
			try {
				String uploadPath = PathConfig.intelliJPath + saveName;
				boolean exists = PathConfig.isExistsPath();
				if(exists) {
					uploadFile.transferTo(new File(PathConfig.realPath(uploadPath)));
				} else {
					uploadPath = PathConfig.eclipsePath + saveName;
					uploadFile.transferTo(new File(PathConfig.realPath(uploadPath)));
				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		food.setUseyn("y");
		foodService.insertFood(food);
		food = foodService.getFoodByMaxFseq();
		FoodDetail foodDetail = new FoodDetail();
		foodDetail.setFood(food);
		foodDetail.setFoodType(foodType);
		foodDetail.setN(n);

		
		for (int i = 0 ; i < ingredient.length ; i++) {
			FoodIngredient fing = new FoodIngredient();
			fing.setFood(food);
			fing.setIngredient(ingredientService.findByName(ingredient[i]).get());
			fing.setAmount(quantity[i]);
			foodDetail.setKcal(fing.getAmount()*fing.getIngredient().getKcal()/100f + foodDetail.getKcal());
			foodDetail.setCarb(fing.getAmount()*fing.getIngredient().getCarb()/100f + foodDetail.getCarb());
			foodDetail.setPrt(fing.getAmount()*fing.getIngredient().getPrt()/100f + foodDetail.getPrt());
			foodDetail.setFat(fing.getAmount()*fing.getIngredient().getFat()/100f + foodDetail.getFat());
			foodIngredientService.insertFoodIngredient(fing);
		}

		foodDetailService.insertFoodDetail(foodDetail);

		re.addAttribute("fseq", food.getFseq());
		List<Food> foodList = new ArrayList<>();
		food = foodService.getFoodByMaxFseq();
		foodDetail = foodDetailService.getFoodDetailByMaxFdseq();
		food.setFoodDetail(foodDetail);
		foodList.add(food);
		dataInOutService.foodListToCsv(foodList);
		return "redirect:admin_food_list";
	}

	// 식품정보 수정 폼
	@PostMapping("admin_food_edit_form")
	public String adminFoodUpdateForm(Food fvo, Model model, HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fvo.getFseq());
		FoodVo foodVo = new FoodVo(food);
		model.addAttribute("kcal", (int)foodVo.getFood().getFoodDetail().getKcal());
		model.addAttribute("carb", String.format("%.2f", foodVo.getFood().getFoodDetail().getCarb()));
		model.addAttribute("prt", String.format("%.2f", foodVo.getFood().getFoodDetail().getPrt()));
		model.addAttribute("fat", String.format("%.2f", foodVo.getFood().getFoodDetail().getFat()));
		model.addAttribute("fileInfo", food.getImg());
		model.addAttribute("foodIngredientList", foodIngredientService.getFoodIngredientListByFood(foodVo.getFood().getFseq()));
		model.addAttribute("foodVo", foodVo);
		return "/admin/foodEdit";
	}

	// 식품정보 수정
	@PostMapping("admin_food_edit")
	public String adminFoodUpdate(RedirectAttributes re, HttpSession session, HttpServletRequest request,
								  @RequestParam(value = "fseq") int fseq, @RequestParam(value = "name") String name,
								  @RequestParam(value = "img") MultipartFile uploadFile, 
								  @RequestParam(value = "ingredient") String[] ingredient,
								  @RequestParam(value = "quantity") int[] quantity,
								  @RequestParam(value = "foodType") String foodType, 
								  @RequestParam(value = "n") int n) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fseq);
		food.setName(name);
		if (!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String saveName = uuid + "_" + fileName;
			food.setImg(saveName);
				try {
					String uploadPath = PathConfig.intelliJPath + saveName;
					boolean exists = PathConfig.isExistsPath();
					if(exists) {
						uploadFile.transferTo(new File(PathConfig.realPath(uploadPath)));
					} else {
						uploadPath = PathConfig.eclipsePath + saveName;
						uploadFile.transferTo(new File(PathConfig.realPath(uploadPath)));
					}
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
		}
		foodService.updateFood(food);
		FoodDetail foodDetail = food.getFoodDetail();
		foodDetail.setFood(food);
		foodDetail.setFoodType(foodType);
		foodDetail.setN(n);
		foodDetail.setKcal(0f);
		foodDetail.setCarb(0f);
		foodDetail.setPrt(0f);
		foodDetail.setFat(0f);
		
		for (FoodIngredient fi : foodIngredientService.getFoodIngredientListByFood(fseq)) {
			foodIngredientService.deleteFoodIngredient(fi);
		}
		
		for (int i = 0 ; i < ingredient.length ; i++) {
			FoodIngredient fing = new FoodIngredient();
			fing.setFood(food);
			fing.setIngredient(ingredientService.findByName(ingredient[i]).get());
			fing.setAmount(quantity[i]);
			foodDetail.setKcal(fing.getAmount()*fing.getIngredient().getKcal()/100f + foodDetail.getKcal());
			foodDetail.setCarb(fing.getAmount()*fing.getIngredient().getCarb()/100f + foodDetail.getCarb());
			foodDetail.setPrt(fing.getAmount()*fing.getIngredient().getPrt()/100f + foodDetail.getPrt());
			foodDetail.setFat(fing.getAmount()*fing.getIngredient().getFat()/100f + foodDetail.getFat());
			foodIngredientService.insertFoodIngredient(fing);
		}

		foodDetailService.insertFoodDetail(foodDetail);

		re.addAttribute("fseq", food.getFseq());
		List<Food> foodList = new ArrayList<>();
		food = foodService.getFoodByMaxFseq();
		foodDetail = foodDetailService.getFoodDetailByMaxFdseq();
		food.setFoodDetail(foodDetail);
		foodList.add(food);
		dataInOutService.foodListToCsv(foodList);
		
		return "redirect:admin_food_detail";
	}

	// 음식 삭제
	@GetMapping("admin_food_delete")
	public String adminFoodDelete(@RequestParam(value = "fseq") int fseq, HttpSession session,
								  HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fseq);
		food.setUseyn("n");
		foodService.insertFood(food);

		return "redirect:admin_food_list";
	}

	// 게시글 리스트 보기
	@GetMapping("/admin_board_list")
	public String adminBoardList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
								 @RequestParam(value = "size", defaultValue = "8") int size,
								 @RequestParam(value = "sortBy", defaultValue = "bseq") String sortBy,
								 @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
								 @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
								 @RequestParam(value = "searchField", defaultValue = "name") String searchField,
								 @RequestParam(value = "searchWord", defaultValue = "") String searchWord, BoardScanVo boardScanVo,
								 HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			boardScanVo.setSearchField(searchField);
			boardScanVo.setSearchWord(searchWord);
			boardScanVo.setSortBy(sortBy);
			boardScanVo.setSortDirection(sortDirection);
			boardScanVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
		}
		Page<Board> boardData = boardService.findBoardList(boardScanVo, page, size);
		boardScanVo.setPageInfo(boardData);
		boardScanVo.setBoardList(boardData.getContent());
		session.setAttribute("boardScanVo", boardScanVo);
		model.addAttribute("boardScanVo", boardScanVo);
		model.addAttribute("boardList", boardScanVo.getBoardList());
		model.addAttribute("pageInfo", boardScanVo.getPageInfo());

		return "admin/boardList";
	}
	// 게시글 검색 보기
	@GetMapping("/admin_board_list_search")
	public String searchBoardList(Model model,
								  @RequestParam(value = "page", defaultValue = "0") int page,
								  @RequestParam(value = "size", defaultValue = "8") int size,
								  @RequestParam(value = "sortBy", defaultValue = "bseq") String sortBy,
								  @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
								  @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
								  @RequestParam(value = "searchField", defaultValue = "") String searchField,
								  @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
								  BoardScanVo boardScanVo,
								  HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			boardScanVo = new BoardScanVo(); // 새로운 객체로 초기화
			boardScanVo.setSearchField(searchField);
			boardScanVo.setSearchWord(searchWord);
			boardScanVo.setSortBy(sortBy);
			boardScanVo.setSortDirection(sortDirection);
			boardScanVo.setPageMaxDisplay(pageMaxDisplay);


		} else {
			boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");

		}

		Page<Board> boardData = boardService.findBoardList(boardScanVo, page, size);

		if (boardData.isEmpty()) {
			model.addAttribute("msg", "검색 결과가 없습니다.");
			model.addAttribute("redirectTo", "/admin_board_list");
			return "board/board_alert";
		} else {

			boardScanVo.setPageInfo(boardData);
			boardScanVo.setBoardList(boardData.getContent());
			session.setAttribute("boardScanVo", boardScanVo);
			model.addAttribute("boardScanVo", boardScanVo);
			model.addAttribute("boardList", boardScanVo.getBoardList());
			model.addAttribute("pageInfo", boardScanVo.getPageInfo());

			return "admin/boardList";
		}
	}


	// 게시글 삭제
	@PostMapping("/admin_board_delete/{bseq}")
	public String adminBoardDelete(@PathVariable(value = "bseq") int bseq, HttpSession session,
								   HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}
		boardCommentsService.deletAllComment(bseq);
		boardService.deleteBoard(bseq);
		return "redirect:/admin_board_list";
	}

	// 게시글 보기
	@GetMapping("/admin_board_detail/{bseq}")
	public String adminBoardDetail(@PathVariable(value = "bseq") int bseq, HttpSession session,
								   HttpServletRequest request, Model model) {
		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Board board = boardService.getBoard(bseq);
		model.addAttribute("board", board);
		BoardScanVo boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
		model.addAttribute("boardScanVo", boardScanVo);
		model.addAttribute("boardList", boardScanVo.getBoardList());
		model.addAttribute("pageInfo", boardScanVo.getPageInfo());

		return "admin/boardDetail";
	}


	@GetMapping("/admin_board_detail/commentsList")
	@ResponseBody
	public Map<String, Object> commentList(@RequestParam(value = "bseq") int bseq) {
		Map<String, Object> result = new HashMap<>();

		// 댓글 목록 가져오기
		List<Comments> parentComments = boardCommentsService.getCommentList(bseq);
		Map<Integer, List<Comments>> commentsMap = new HashMap<>();

		// 부모 댓글마다 대댓글 목록 가져오기
		for (Comments parentComment : parentComments) {
			List<Comments> replies = boardCommentsService.getReplyCommentList(parentComment.getCseq());
			commentsMap.put(parentComment.getCseq(), replies);
		}
		// 대댓글을 포함한 댓글 수 계산
		int totalCommentCount = parentComments.size(); // 부모 댓글 수를 초기화
		for (List<Comments> replies : commentsMap.values()) {
			totalCommentCount += replies.size(); // 각 부모 댓글에 대한 대댓글 수를 더함
		}
		result.put("parentComments", parentComments);
		result.put("repliesMap", commentsMap);

		return result;
	}

	@ResponseBody
	@GetMapping(value = "download")
	public ResponseEntity<String> download() {

		List<Users> user = usersService.findAll();

		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "text/csv; charset=MS949");
		header.add("Content-Disposition", "attachment; filename=\"" + "user.csv" + "\"");

		return new ResponseEntity<String>(setContent(user), header, HttpStatus.CREATED);
	}

	public String setContent(List<Users> user) {
		String data = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		data += "나이, 신장, 성별, useq,체중, 아이디, 비밀번호, 이름, 데이터기준일 : " + sdf.format(new Date()) + "\n";
		for (int i = 0; i < user.size(); i++) {
			data += user.get(i).getAge() + ",";
			data += user.get(i).getHeight() + ",";
			data += user.get(i).getSex() + ",";
			data += user.get(i).getUseq() + ",";
			data += user.get(i).getWeight() + ",";
			data += user.get(i).getUserid() + ",";
			data += user.get(i).getUserpw() + ",";
			data += user.get(i).getName() + ",\n";

		}
		return data;
	}
	// 사용자에따라 파이썬파일 경로 변경 필요
	public void foodInsertPythonRun(FoodDetail foodDetail, Food food) {
		ProcessBuilder processBuilder = new ProcessBuilder("python", "E:/Student/MachineLearning/new_food_insert.py",
				Integer.toString(food.getFseq()), food.getName(), Double.toString(foodDetail.getKcal()),
				Double.toString(foodDetail.getCarb()), Double.toString(foodDetail.getPrt()),
				Double.toString(foodDetail.getFat()), foodDetail.getFoodType());

		try {
			Process process = processBuilder.start();
			System.out.println("파이썬 실행 완료");
			InputStream inputStream = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				// 실행 결과 출력(python파일에서의 print()값
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/admin_exercise_list")
	public String adminExerciseWrite(){
		return "admin/exercise";
	}
	@PostMapping("/admin_exercise_list")
	public String showExerciseList(@RequestParam (value = "exerciseType") String exerciseType,
								   @RequestParam (value= "exerciseFomula") String exerciseFomula) {
		ExerciseOption eo = new ExerciseOption();
		eo.setType(exerciseType);
		eo.setFomula(exerciseFomula);
		exerciseOptionService.insertExerciseOption(eo);

		return "/admin/exercise";
	}

	@GetMapping("/checkExerciseType")
	@ResponseBody
	public Map<String, Boolean> checkExerciseType(@RequestParam String exerciseType) {
		boolean isDuplicated = exerciseOptionRepository.existsByType(exerciseType);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicated", isDuplicated);
		return response;
	}

	// 음식 추가 시 영양성분 계산해주는 메소드
	@PostMapping("/admin_igrd_cal")
	@ResponseBody
	public Map<String, Object> calculateIngredient(@RequestBody List<CalData> calDataList) {
		float kcal = 0f;
		float carb = 0f;
		float prt = 0f;
		float fat = 0f;
		//calDataList.remove(0);
		if(!calDataList.isEmpty()) {
			for (CalData calData : calDataList) {
				String igredientName = calData.name;
				int quantity = calData.amount;
				Ingredient igrd = ingredientService.findByName(igredientName).get();
				kcal += igrd.getKcal() * (quantity / 100f);
				carb += igrd.getCarb() * (quantity / 100f);
				prt += igrd.getPrt() * (quantity / 100f);
				fat += igrd.getFat() * (quantity / 100f);
			}
		}
		Map<String, Object> response = new HashMap<>();
		response.put("kcal", (int)kcal);
		response.put("carb", String.format("%.2f", carb));
		response.put("prt",  String.format("%.2f", prt));
		response.put("fat",  String.format("%.2f", fat));
		return response;
	}

	/**
	 *  음식 추가 시 ajax로 보낸 값 받는 클래스--------------------------------------------------------------
	 */
	@Getter
	@Setter
	class CalData {
		private String name;
		private int amount;
	}

}
