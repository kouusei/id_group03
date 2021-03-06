package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

		//Accountテーブルから指定したレコードを取得
		List<Account> record = accountRepository.findByEmailLikeAndPasswordLike(email, password);

		if (record.size() == 0) {
			mv.addObject("error", "メールアドレスかパスワードが");
			mv.addObject("error2", "一致しません");
			mv.setViewName("login");
			return mv;
		} else {

			Account customerInfo = record.get(0);
			List<Sche> records = scheRepository.findAllByOrderByScheduledateAsc();
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
	public ModelAndView forget(
			ModelAndView mv) {

		mv.setViewName("forget");

		return mv;
	}

	//パスワード忘れ画面
	@PostMapping("/forget")
	public ModelAndView reforget(
			@RequestParam("email") String email,
			ModelAndView mv) {

		List<Account> _email = accountRepository.findByEmailLike(email);

		if (_email.size() == 0) {

			mv.addObject("error", "メールアドレスが");
			mv.addObject("error2", "登録されていません");
			mv.setViewName("forget");
			return mv;

		} else {

			Account account = _email.get(0);
			mv.addObject("account", account);
			mv.setViewName("forget_confirm");
			return mv;
		}

	}

	//パスワード再設定画面遷移
	@RequestMapping("/forget_confirm")
	public ModelAndView forget_confirm(
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		List<Account> account = accountRepository.findBySecretLikeAndAnswerLike(secret, answer);

		if (account.size() == 0) {

			Account _account = new Account(secret);
			mv.addObject("error", "回答が間違っています");
			mv.addObject("account", _account);
			mv.setViewName("forget_confirm");
			return mv;

		} else {

			Account _account = account.get(0);
			mv.addObject("account", _account);
			mv.setViewName("reset");

			return mv;
		}

	}

	//パスワード再設定画面
	@PostMapping("/reset")
	public ModelAndView reset(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		if (!password.equals(password2)) {

			Account account = new Account(code, name, email, secret, answer);
			mv.addObject("account", account);
			mv.addObject("error", "パスワードが");
			mv.addObject("error2", "一致していません");
			mv.setViewName("reset");
			return mv;

		} else {

			Account account = new Account(code, name, email, password, secret, answer);
			accountRepository.saveAndFlush(account);
			mv.addObject("account", account);
			mv.addObject("error", "パスワードが");
			mv.addObject("error2", "再設定されました");
			mv.setViewName("reset");
			return mv;
		}

	}

	//パスワード再設定からログイン画面に戻る
	@RequestMapping("/login/back")
	public ModelAndView login_back(ModelAndView mv) {

		mv.setViewName("login");
		return mv;
	}

	//新規登録確認
	@PostMapping("/signup_confirm")
	public ModelAndView signup_error(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		List<Account> Name = accountRepository.findByName(name);
		List<Account> Email = accountRepository.findByEmailLike(email);

		//未入力、パスワードが違えばエラー表示
		if (Name.size() != 0) {
			mv.addObject("error", "既にこの名前は");
			mv.addObject("error2", "登録されています");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.addObject("password2", password2);
			mv.addObject("secret", secret);
			mv.addObject("answer", answer);
			mv.setViewName("signup");
			return mv;

		} else if (Email.size() != 0) {
			mv.addObject("error", "既にこのメールアドレスは");
			mv.addObject("error2", "登録されています");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.addObject("password2", password2);
			mv.addObject("secret", secret);
			mv.addObject("answer", answer);
			mv.setViewName("signup");
			return mv;

		} else if (!password.equals(password2)) {
			mv.addObject("error", "パスワードが一致していません");
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
			mv.addObject("password2", password2);
			mv.addObject("secret", secret);
			mv.addObject("answer", answer);
			mv.setViewName("signup");
			return mv;

		} else {
			mv.addObject("name", name);
			mv.addObject("email", email);
			mv.addObject("password", password);
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
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {
		//登録するAccountエンティティのインスタンスを生成
		Account account = new Account(name, email, password, secret, answer);

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
		String times[] = { "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
				"05:00", "05:30",
				"06:00",
				"06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
				"12:00",
				"12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
				"18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00",
				"23:30" };
		mv.addObject("times", times);
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

		List<Sche> records = scheRepository.findAllByOrderByScheduledateAsc();
		mv.addObject("records", records);

		mv.setViewName("calender");

		return mv;
	}

	//お客様情報に遷移
	@RequestMapping("/customer/{customerInfo.name}")
	public ModelAndView customer(
			@PathVariable(name = "customerInfo.name") String name,
			ModelAndView mv) {

		Account accountList = accountRepository.findByNameLike(name);

		mv.addObject("account", accountList);
		mv.setViewName("customer");

		return mv;
	}

	//お客様情報画面からパスワードの再設定
	@RequestMapping("/resetpass")
	public ModelAndView resetpass(
			@RequestParam("name") String name,
			ModelAndView mv) {

		Account account = accountRepository.findByNameLike(name);
		mv.addObject("account", account);
		mv.setViewName("passreset");
		return mv;
	}

	//お客様情報画面からパスワードの再設定
	@GetMapping("/resetpass")
	public ModelAndView resetpass2(
			@RequestParam("name") String name,
			ModelAndView mv) {

		Account account = accountRepository.findByNameLike(name);
		mv.addObject("account", account);
		mv.setViewName("customer");
		return mv;
	}

	//パスワード再設定画面
	@RequestMapping("/passreset")
	public ModelAndView passreset(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("password2") String password2,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		if (!password.equals(password2)) {

			Account account = new Account(code, name, email, secret, answer);
			mv.addObject("account", account);
			mv.addObject("error", "パスワードが");
			mv.addObject("error2", "一致していません");
			mv.setViewName("passreset");
			return mv;

		} else {

			Account account = new Account(code, name, email, password, secret, answer);
			accountRepository.saveAndFlush(account);
			mv.addObject("account", account);
			mv.addObject("error", "パスワードが");
			mv.addObject("error2", "再設定されました");
			mv.setViewName("passreset");
			return mv;
		}

	}

	//お客様情報からカレンダーに戻る
	@RequestMapping("/edit/calender")
	public ModelAndView edit_customer(
			ModelAndView mv) {

		List<Sche> records = scheRepository.findAllByOrderByScheduledateAsc();
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
		starthour = starthour.length() == 1 ? "0" + starthour : starthour;
		endhour = endhour.length() == 1 ? "0" + endhour : endhour;
		Account account = (Account) session.getAttribute("customerInfo");
		MyDate hizuke = (MyDate) session.getAttribute("sche");
		int category_code = account.getCode();
		int Start = Integer.parseInt(starthour);
		int End = Integer.parseInt(endhour);
		int starT = Integer.parseInt(startminute);
		int enD = Integer.parseInt(endminute);

		if (year == "" || month == "" || date == "" || starthour == "" || startminute == "" || endhour == ""
				|| endminute == "" || schedule == "") {

			String times[] = { "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
					"05:00", "05:30",
					"06:00",
					"06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
					"12:00",
					"12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
					"18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00",
					"23:30" };
			mv.addObject("times", times);
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
			String times[] = { "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
					"05:00", "05:30",
					"06:00",
					"06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
					"12:00",
					"12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
					"18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00",
					"23:30" };
			mv.addObject("times", times);
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
			String times[] = { "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
					"05:00", "05:30",
					"06:00",
					"06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
					"12:00",
					"12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
					"18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00",
					"23:30" };
			mv.addObject("times", times);
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

		String times[] = { "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
				"05:00", "05:30",
				"06:00",
				"06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
				"12:00",
				"12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
				"18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00",
				"23:30" };
		mv.addObject("times", times);
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
	@RequestMapping("/edit")
	public ModelAndView customer_edit(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,

			ModelAndView mv) {

		//Thymeleafで表示する準備
		Account account = new Account(code, name, email, password, secret, answer);
		mv.addObject("account", account);
		mv.setViewName("edit");
		return mv;

	}

	//お客様情報変更確認
	@PostMapping("/customer_confirm")
	public ModelAndView customer_confirm(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		Account account = new Account(code, name, email, password, secret, answer);

		mv.addObject("account", account);
		mv.addObject("error", "以下の情報でよろしいですか?");
		mv.setViewName("customer_confirm");
		return mv;

	}

	//お客様情報変更後戻る
	@PostMapping("/customer_change")
	public ModelAndView edit(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		//登録するAccountエンティティのインスタンスを生成
		Account account = new Account(code, name, email, password, secret, answer);
		accountRepository.saveAndFlush(account);

		Account customerInfo = accountRepository.findByNameLike(name);

		session.setAttribute("customerInfo", customerInfo);
		mv.addObject("customerInfo", customerInfo);

		mv.addObject("account", account);
		mv.addObject("error", "変更が完了しました");

		mv.setViewName("customer");
		return mv;

	}

	//パスワード再設定からお客様情報に戻る
	@RequestMapping("/customerback")
	public ModelAndView customerback(
			@RequestParam("code") int code,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("secret") String secret,
			@RequestParam("answer") String answer,
			ModelAndView mv) {

		//登録するAccountエンティティのインスタンスを生成
		Account account = new Account(code, name, email, password, secret, answer);
		mv.addObject("account", account);

		mv.setViewName("customer");
		return mv;

	}

}