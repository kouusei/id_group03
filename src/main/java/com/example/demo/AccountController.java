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
			mv.addObject("error", "未入力項目があります");
			mv.setViewName("login");
			return mv;

		} else {
			//Accountテーブルから指定したレコードを取得
			List<Account> record = accountRepository.findByEmailLikeAndPasswordLike(email, password);

			if (record.size() == 0) {
				mv.addObject("error", "メールアドレスかパスワードが");
				mv.addObject("error2", "一致しません");
				mv.setViewName("login");
				return mv;
			}

			Account customerInfo = record.get(0);
			List<Sche> records = scheRepository.findAll();
			mv.addObject("records", records);

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

	//パスワード忘れ画面
	@RequestMapping("/forget")
	public ModelAndView forget(ModelAndView mv) {

		mv.setViewName("forget");

		return mv;
	}

	//パスワード忘れ画面
	@RequestMapping("/forget_confirm")
	public ModelAndView forget_confirm(ModelAndView mv) {

		mv.setViewName("forget_confirm");

		return mv;
	}

	//新規登録後
	@PostMapping("/signup_confirm")
	public ModelAndView signup_error(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("tel") String tel,
			@RequestParam("address") String address,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		//未入力、パスワードが違えばエラー表示
		if (name == "" || email == "" || password == "" || password2 == "" || tel == "" || address == "" || secret == ""
				|| answer == "") {
			mv.addObject("error", "未入力項目があります。");
			mv.setViewName("signup");
			return mv;

		} else if (!password.equals(password2)) {
			mv.addObject("error", "パスワードが一致していません");
			mv.setViewName("signup");
			return mv;

		} else {
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.addObject("tel", tel);
			mv.addObject("address", address);
			mv.addObject("secret", secret);
			mv.addObject("answer", answer);
			mv.addObject("error", "以下の情報でよろしいですか?");

			mv.setViewName("signup_confirm");
			return mv;

		}
	}

	//新規登録確認
	@PostMapping("/signup")
	public ModelAndView signup_confirm(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("tel") String tel,
			@RequestParam("address") String address,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {
		//登録するAccountエンティティのインスタンスを生成
		Account account = new Account(name, email, password, tel, address, secret, answer);

		accountRepository.saveAndFlush(account);
		mv.addObject("error", "新規登録できました。");

		mv.setViewName("login");

		return mv;
	}

	//スケジュール画面へ遷移
	@RequestMapping("/calender/{date}/{month}/{year}")
	public ModelAndView calender(
			@PathVariable(name = "date") String date,
			@PathVariable(name = "month") String month,
			@PathVariable(name = "year") String year,

			ModelAndView mv) {
		month = month.length() == 1 ? "0" + month : month;
		date = date.length() == 1 ? "0" + date : date;
		String scheduledate = year + "/" + month + "/" + date;
		String scheduledates = year + "/" + month;

		List<Sche> records = scheRepository.findByScheduledate(scheduledate);
		List<Sche> recordz = scheRepository.findByScheduledateLikeOrderByScheduledateAsc("%" + scheduledates + "%");
		mv.addObject("records", records);
		mv.addObject("recordz", recordz);

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
			@RequestParam("DATE") String date,
			@RequestParam("MONTH") String month,
			@RequestParam("YEAR") String year,
			ModelAndView mv) {
		//登録するDateのインスタンスを生成
		MyDate cale = new MyDate(date, month, year);
		mv.addObject("cale", cale);

		List<Sche> records = scheRepository.findAll();
		mv.addObject("records", records);

		mv.setViewName("calender");

		return mv;
	}

	//お客様情報に遷移
	@RequestMapping("/customer/{customerInfo.name}")
	public ModelAndView customer(
			@PathVariable(name = "customerInfo.name") String name,
			ModelAndView mv) {

		Account accountList = accountRepository.findByName(name);

		mv.addObject("account", accountList);
		mv.setViewName("customer");

		return mv;
	}

	//お客様情報からカレンダーに戻る
	@RequestMapping("/edit/calender")
	public ModelAndView customer(ModelAndView mv) {

		List<Sche> records = scheRepository.findAll();
		mv.addObject("records", records);

		mv.setViewName("calender");

		return mv;
	}

	//スケジュールを追加
	@PostMapping("/update")
	public ModelAndView update(
			@RequestParam("YEAR") String year,
			@RequestParam("MONTH") String month,
			@RequestParam("DAY") String date,
			@RequestParam("starthour") String starthour,
			@RequestParam("startminute") String startminute,
			@RequestParam("endhour") String endhour,
			@RequestParam("endminute") String endminute,
			@RequestParam("schedule") String schedule,
			@RequestParam("schedulememo") String schedulememo,
			ModelAndView mv) {

		Account account = (Account) session.getAttribute("customerInfo");
		MyDate hizuke = (MyDate) session.getAttribute("sche");
		int category_code = account.getCode();
		int Start = Integer.parseInt(starthour);
		int End = Integer.parseInt(endhour);
		int starT = Integer.parseInt(startminute);
		int enD = Integer.parseInt(endminute);

		if (year == "" || month == "" || date == "" || starthour == "" || startminute == "" || endhour == ""
				|| endminute == "" || schedule == "") {

			month = month.length() == 1 ? "0" + month : month;
			date = date.length() == 1 ? "0" + date : date;
			String scheduledate = year + "/" + month + "/" + date;
			String scheduledates = year + "/" + month;

			//scheテーブルから指定したレコードを取得(全レコード取得)
			List<Sche> records = scheRepository.findByScheduledate(scheduledate);
			List<Sche> recordz = scheRepository.findByScheduledateLikeOrderByScheduledateAsc("%" + scheduledates + "%");
			session.setAttribute("records", records);
			mv.addObject("sche", hizuke);
			mv.addObject("records", records);
			mv.addObject("recordz", recordz);
			mv.addObject("year", year);
			mv.addObject("month", month);
			mv.addObject("date", date);
			mv.addObject("error", "未入力項目があります。");

			mv.setViewName("schedule");
			return mv;

		} else if (Start + starT > End + enD) {

			month = month.length() == 1 ? "0" + month : month;
			date = date.length() == 1 ? "0" + date : date;
			String scheduledate = year + "/" + month + "/" + date;
			String scheduledates = year + "/" + month;

			//scheテーブルから指定したレコードを取得(全レコード取得)
			List<Sche> records = scheRepository.findByScheduledate(scheduledate);
			List<Sche> recordz = scheRepository.findByScheduledateLikeOrderByScheduledateAsc("%" + scheduledates + "%");
			session.setAttribute("records", records);
			mv.addObject("sche", hizuke);
			mv.addObject("records", records);
			mv.addObject("recordz", recordz);
			mv.addObject("year", year);
			mv.addObject("month", month);
			mv.addObject("date", date);
			mv.addObject("error", "時間設定が間違っています。");

			mv.setViewName("schedule");
			return mv;

		} else {

			month = month.length() == 1 ? "0" + month : month;
			date = date.length() == 1 ? "0" + date : date;
			String scheduledate = year + "/" + month + "/" + date;
			String scheduledates = year + "/" + month;
			String starttime = starthour + ":" + startminute;
			String endtime = endhour + ":" + endminute;

			//登録するDateのインスタンスを生成
			Sche update = new Sche(category_code, scheduledate, starttime, endtime, schedule, schedulememo);
			scheRepository.saveAndFlush(update);

			//scheテーブルから指定したレコードを取得(全レコード取得)
			List<Sche> records = scheRepository.findByScheduledate(scheduledate);
			List<Sche> recordz = scheRepository.findByScheduledateLikeOrderByScheduledateAsc("%" + scheduledates + "%");
			session.setAttribute("records", records);
			mv.addObject("sche", hizuke);
			mv.addObject("year", year);
			mv.addObject("month", month);
			mv.addObject("date", date);
			mv.addObject("records", records);
			mv.addObject("recordz", recordz);

			mv.setViewName("schedule");
			return mv;

		}

	}

	//削除の処理
	@RequestMapping("/customer/delete")
	public ModelAndView delete(
			@RequestParam("code") int code,
			@RequestParam("date") String date,
			@RequestParam("month") String month,
			@RequestParam("year") String year,
			ModelAndView mv) {

		scheRepository.deleteById(code);

		month = month.length() == 1 ? "0" + month : month;
		date = date.length() == 1 ? "0" + date : date;
		String scheduledate = year + "/" + month + "/" + date;
		String scheduledates = year + "/" + month;

		List<Sche> records = scheRepository.findByScheduledate(scheduledate);
		List<Sche> recordz = scheRepository.findByScheduledateLikeOrderByScheduledateAsc("%" + scheduledates + "%");
		mv.addObject("records", records);
		mv.addObject("recordz", recordz);
		mv.addObject("year", year);
		mv.addObject("month", month);
		mv.addObject("date", date);

		//登録するDateのインスタンスを生成
		MyDate sche = new MyDate(date, month, year);
		session.setAttribute("sche", sche);
		mv.addObject("sche", sche);

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
			@RequestParam("tel") String tel,
			@RequestParam("address") String address,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,

			ModelAndView mv) {

		//Thymeleafで表示する準備
		mv.addObject("code", code);
		mv.addObject("name", name);
		mv.addObject("email", email);
		mv.addObject("tel", tel);
		mv.addObject("address", address);
		mv.addObject("secret", secret);
		mv.addObject("answer", answer);

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
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		//未入力、パスワードが違えばエラー表示
		if (name == "" || email == "" || tel == "" || address == "" || secret == "" || answer == "") {
			mv.addObject("error", "未入力項目があります。");

			mv.setViewName("edit");
			return mv;

		} else {
			Account acc = (Account) session.getAttribute("customerInfo");
			session.setAttribute("customerInfo", acc);

			//登録するAccountエンティティのインスタンスを生成
			Account account = new Account(code, name, email, tel, address, secret, answer);
			accountRepository.saveAndFlush(account);

			mv.addObject("account", account);
			mv.addObject("error", "変更が完了しました。");

			mv.setViewName("customer");
			return mv;

		}
	}

}