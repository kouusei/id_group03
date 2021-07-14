package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {

	@Autowired
	HttpSession session;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ScheduleRepository scheduleRepository;

	@Autowired
	ScheRepository scheRepository;

	//トップ画面
	@RequestMapping("/")
	public ModelAndView top(ModelAndView mv) {
		Account ACC = (Account) session.getAttribute("login");
		mv.addObject("login", ACC);

		mv.setViewName("top");

		return mv;
	}

	//ログイン画面
	@RequestMapping("/login")
	public ModelAndView login(ModelAndView mv) {

		mv.setViewName("login");

		return mv;
	}

	//ログイン結果
	@PostMapping("/login")
	public ModelAndView login_after(
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			ModelAndView mv) {

		//emailとpasswordがからの場合エラー
		if (email == null || email.length() == 0 || password == null || password.length() == 0) {
			mv.addObject("end", "メールアドレスとパスワードを入力してください");
			mv.setViewName("login");
			return mv;

		} else {
			//Accountテーブルから指定したレコードを取得
			List<Account> record = accountRepository.findByEmailLikeAndPasswordLike(email, password);

			if (record.size() == 0) {
				mv.addObject("error", "メールアドレスかパスワードが一致しません");
				mv.setViewName("login");
				return mv;
			}

			Account customerInfo = record.get(0);
			//List<Schedule> record2 = scheduleRepository.findByScheduledateLike("%" +2021+ "%");
			List<Account> record2 = accountRepository.findAll();
			mv.addObject("AAA", 1);
			mv.addObject("record", record2);

			// セッションスコープにログイン名とカテゴリ情報を格納する
			session.setAttribute("customerInfo", customerInfo);
			mv.setViewName("calender");

			return mv;
		}
	}

	//新規登録画面
	@RequestMapping("/signup")
	public ModelAndView signup(ModelAndView mv) {

		mv.setViewName("signup");

		return mv;
	}

	//新規登録後
	@PostMapping("/signup")
	public ModelAndView signup_error(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("tel") String tel,
			@RequestParam("address") String address,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2,
			ModelAndView mv) {

		//未入力、パスワードが違えばエラー表示
		if (name == "" || email == "" || password == "" || password2 == "" || tel == "" || address == "") {
			mv.addObject("error", "未入力項目があります。");
			mv.setViewName("signup");
			return mv;

		} else if (!password.equals(password2)) {
			mv.addObject("error", "パスワードが一致していません");
			mv.setViewName("signup");
			return mv;

		} else {
			//登録するAccountエンティティのインスタンスを生成
			Account account = new Account(name, email, password, tel, address);

			accountRepository.saveAndFlush(account);
			mv.addObject("error", "新規登録できました。");
			mv.setViewName("login");
			return mv;

		}
	}

	//スケジュール画面へ遷移
	@RequestMapping("/calender/{date}/{month}/{year}")
	public ModelAndView calender(
			@PathVariable(name = "date") int date,
			@PathVariable(name = "month") int month,
			@PathVariable(name = "year") int year,
			ModelAndView mv) {
		List<Sche> records = scheRepository.findAll();
		System.out.println(records);
		System.out.println(records.size());
		mv.addObject("records", records);
		//登録するDateのインスタンスを生成
		MyDate sche = new MyDate(date, month, year);
		session.setAttribute("sche", sche);
		mv.addObject("sche", sche);
		mv.setViewName("schedule");
		return mv;
	}

	//カレンダー画面へ戻る
	@PostMapping("/calender")
	public ModelAndView returncalender(
			@RequestParam("DATE") int date,
			@RequestParam("MONTH") int month,
			@RequestParam("YEAR") int year,
			ModelAndView mv) {
		//登録するDateのインスタンスを生成
		MyDate cale = new MyDate(date, month, year);
		//Schedule schedule = (Schedule) session.getAttribute("record2");
		//mv.addObject("schedule",schedule);

		mv.addObject("cale", cale);
		mv.addObject("AAA", 1);
		mv.setViewName("calender");

		return mv;
	}

	//カレンダー画面へ戻る2
	@RequestMapping("/calender/{sche.month}/{sche.year}")
	public ModelAndView returncalender2(
			@PathVariable(name = "sche.month") int month,
			@PathVariable(name = "sche.year") int year,
			ModelAndView mv) {

		//MyDate schedule = (MyDate) session.getAttribute("sche");
//		int smonth = schedule.getMonth();
//		int syear = schedule.getYear();
		//登録するDateのインスタンスを生成
		MyDate cales = new MyDate(month, year);
		//mv.addObject("schedule",schedule);
		mv.addObject("cales", cales);
//		mv.addObject("smonth", smonth);
//		mv.addObject("syear", syear);
		mv.setViewName("calender");

		return mv;
	}

	//お客様情報に遷移
	@RequestMapping("/customer/{customerInfo.name}")
	public ModelAndView customer(
			@PathVariable(name = "customerInfo.name") String name,
			ModelAndView mv) {

		Account account = (Account) session.getAttribute("customerInfo");
		String password = account.getPassword();

		Account accountList = accountRepository.findByName(name);

		mv.addObject("account", accountList);
		mv.addObject("pass", password);

		mv.setViewName("customer");

		return mv;
	}

	//お客様情報からカレンダーに戻る
	@RequestMapping("/calender")
	public ModelAndView customer(ModelAndView mv) {

		Account acc = (Account) session.getAttribute("customerInfo");
		session.setAttribute("customerInfo2", acc);
		mv.setViewName("calender");

		return mv;
	}

//	//スケジュールを追加
//	@PostMapping("/update")
//	public ModelAndView update(
//			@RequestParam("YEAR") String year,
//			@RequestParam("MONTH") String month,
//			@RequestParam("DAY") String day,
//			@RequestParam("starthour") String starthour,
//			@RequestParam("startminute") String startminute,
//			@RequestParam("endhour") String endhour,
//			@RequestParam("endminute") String endminute,
//			@RequestParam("schedule") String schedule,
//			@RequestParam("schedulememo") String schedulememo,
//			ModelAndView mv) {
//		Account account = (Account) session.getAttribute("customerInfo");
//		int category_code = account.getCode();
//		try {
//			int y = Integer.parseInt(year);
//			int m = Integer.parseInt(month);
//			int d = Integer.parseInt(day);
//			int _y = y-1900;
//			int _m = m-1;
////			String strDate = "2019/11/01";
////			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
//			@SuppressWarnings("deprecation")
//			Date scheduledate =  new Date(_y, _m, d);
//			System.out.println(scheduledate);
//			String stime = starthour + ":" + startminute;
//			String etime = endhour + ":" + endminute;
//			Time starttime = Time.valueOf("10:10");
//			Time endtime = Time.valueOf("11:10");
////			Time.valueOf("10:20");
//			//登録するDateのインスタンスを生成
//			Schedule update = new Schedule(category_code, scheduledate, starttime, endtime, schedule, schedulememo);
//			scheduleRepository.saveAndFlush(update);
//			//scheduleテーブルから指定したレコードを取得
//			List<Schedule> records = scheduleRepository.findBySchedule(schedule);
//			session.setAttribute("records", records);
//			mv.addObject("AAA", 1);
//			mv.addObject("records", records);
//			mv.setViewName("calender");
//			return mv;
//
//		} catch (Exception e) {
//            e.printStackTrace();
//            mv.setViewName("calender");
//            return mv;
//        }
//
//	}

	//スケジュールを追加
		@PostMapping("/update")
		public ModelAndView update(
				@RequestParam("YEAR") String year,
				@RequestParam("MONTH") String month,
				@RequestParam("DAY") String day,
				@RequestParam("starthour") String starthour,
				@RequestParam("startminute") String startminute,
				@RequestParam("endhour") String endhour,
				@RequestParam("endminute") String endminute,
				@RequestParam("schedule") String schedule,
				@RequestParam("schedulememo") String schedulememo,
				ModelAndView mv) {

				Account account = (Account) session.getAttribute("customerInfo");
				MyDate hizuke = (MyDate)session.getAttribute("sche");
				int category_code = account.getCode();

				String scheduledate = year + "/" + month + "/" + day;
				String starttime = starthour + ":" + startminute;
				String endtime = endhour + ":" + endminute;
				//登録するDateのインスタンスを生成
				Sche update = new Sche(category_code, scheduledate, starttime, endtime, schedule, schedulememo);
				scheRepository.saveAndFlush(update);

				//scheテーブルから指定したレコードを取得(全レコード取得)
				List<Sche> records = scheRepository.findAll();
				session.setAttribute("records", records);
				mv.addObject("sche", hizuke);
				mv.addObject("records", records);
				mv.setViewName("schedule");
				return mv;

			}

	//ログアウト処理
	@RequestMapping("/logout")
	public ModelAndView logout(ModelAndView mv) {

		session.invalidate();

		mv.setViewName("login");
		return mv;
	}

	//お客様情報変更画面遷移
	@PostMapping("/edit")
	public ModelAndView customer_edit(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("tel") String tel,
			@RequestParam("address") String address,
			ModelAndView mv) {

		//Thymeleafで表示する準備
		mv.addObject("code", code);
		mv.addObject("name", name);
		mv.addObject("email", email);
		mv.addObject("password", password);
		mv.addObject("tel", tel);
		mv.addObject("address", address);

		mv.setViewName("edit");
		return mv;

	}

	//お客様情報変更後戻る
	@PostMapping("/customer")
	public ModelAndView edit(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("tel") String tel,
			@RequestParam("address") String address,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2,
			ModelAndView mv) {

		//未入力、パスワードが違えばエラー表示
		if (name == "" || email == "" || password == "" || password2 == "" || tel == "" || address == "") {
			mv.addObject("error", "未入力項目があります。");
			mv.setViewName("edit");
			return mv;

		} else if (!password.equals(password2)) {
			mv.addObject("error", "パスワードが一致していません");
			mv.setViewName("edit");
			return mv;

		} else {
			Account acc = (Account) session.getAttribute("customerInfo");
			session.setAttribute("customerInfo2", acc);

			//登録するAccountエンティティのインスタンスを生成
			Account account = new Account(code, name, email, password, tel, address);
			accountRepository.saveAndFlush(account);

			mv.addObject("account", account);
			mv.addObject("error", "変更が完了しました。");
			mv.setViewName("customer");
			return mv;

		}
	}

}