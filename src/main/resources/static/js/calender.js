/*
カレンダー作成
*/

//カレンダーの年範囲を作成
function generate_year_range(start, end) {
  var years = "";
  for (var year = start; year <= end; year++) {
      years += "<option value='" + year + "'>" + year + "</option>";
  }
  return years;
}

var today = new Date(); //日付
var currentMonth = today.getMonth(); //月
var currentYear = today.getFullYear(); //年
var selectYear = document.getElementById("year"); //年の選択
var selectMonth = document.getElementById("month"); //月の選択

var createYear = generate_year_range(1970, 2200); //カレンダーの年範囲

document.getElementById("year").innerHTML = createYear; //指定した範囲の年を作成

var calendar = document.getElementById("calendar"); //カレンダー本体
var lang = calendar.getAttribute('data-lang'); //言語

var months = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]; //月
var days = ["日", "月", "火", "水", "木", "金", "土"]; //曜日

//日付を表示
var dayHeader = "<tr>";
for (day in days) {
  dayHeader += "<th data-days='" + days[day] + "'>" + days[day] + "</th>";
}
dayHeader += "</tr>";

//各曜日をカレンダーの上部に表示
document.getElementById("thead-month").innerHTML = dayHeader;

//現在の（選択した）年月を表示
monthAndYear = document.getElementById("monthAndYear");
showCalendar(currentMonth, currentYear);

//nextボタンを押すと、1か月後のカレンダーを表示
function next() {
  currentYear = (currentMonth === 11) ? currentYear + 1 : currentYear;
  currentMonth = (currentMonth + 1) % 12;
  showCalendar(currentMonth, currentYear);
}

//previousボタンを押すと、1か月前のカレンダーを表示
function previous() {
  currentYear = (currentMonth === 0) ? currentYear - 1 : currentYear;
  currentMonth = (currentMonth === 0) ? 11 : currentMonth - 1;
  showCalendar(currentMonth, currentYear);
}

//選択した年月にジャンプ
function jump() {
  currentYear = parseInt(selectYear.value);
  currentMonth = parseInt(selectMonth.value);
  showCalendar(currentMonth, currentYear);
}

//カレンダーの本体部分の表示
function showCalendar(month, year) {

  var firstDay = ( new Date( year, month ) ).getDay();

  tbl = document.getElementById("calendar-body");

  tbl.innerHTML = "";

  monthAndYear.innerHTML = months[month] + " " + year;
  selectYear.value = year;
  selectMonth.value = month;

  // creating all cells（カレンダーを格納するセルの作成）
  var date = 1;

  for ( var i = 0; i < 6; i++ ) {
      var row = document.createElement("tr");

      for ( var j = 0; j < 7; j++ ) {
          if ( i === 0 && j < firstDay ) {
              cell = document.createElement("td");
              cellText = document.createTextNode("");
              cell.appendChild(cellText);
              row.appendChild(cell);
          } else if (date > daysInMonth(month, year)) {
              break;
          } else {
          	  //セル生成
              cell = document.createElement("td");

              cell.className = "date-picker";

              //セルに日付を入れる
              cell.innerHTML = "<span>" +'<a href="/calender/'+ date +"/" + (month+1) + "/"+ year + '">'+ date +"</a>" + "</span>";

              if ( date === today.getDate() && year === today.getFullYear() && month === today.getMonth() ) {
                  cell.className = "date-picker selected";
              }
              row.appendChild(cell);
              date++;
          }
      }

      tbl.appendChild(row);
  }

}

//1か月の日数を設定
function daysInMonth(iMonth, iYear) {
  return 32 - new Date(iYear, iMonth, 32).getDate();
}