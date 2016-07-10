package net.oschina.app.v2.ui.calendar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

/**
 * 日历gridview中的每一个item显示的textview
 * @author lmw
 *
 */
public class CalendarAdapter extends BaseAdapter {
	private boolean isLeapyear = false;  //是否为闰年
	private int daysOfMonth = 0;      //某月的天数
	private int dayOfWeek = 0;        //具体某一天是星期几
	private int lastDaysOfMonth = 0;  //上一个月的总天数
	private Context context;
	private String[] dayNumber = new String[42];  //一个gridview中的日期存入此数组中
//	private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
	private SpecialCalendar sc = null;
	//private LunarCalendar lc = null;
	private Resources res = null;
	//private Drawable drawable = null;
	
	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1;     //用于标记当天
	private int[] schDateTagFlag = null;  //存储当月所有的日程日期
	
	private String showYear = "";   //用于在头部显示的年份
	private String showMonth = "";  //用于在头部显示的月份
	private String animalsYear = ""; 
	private String leapMonth = "";   //闰哪一个月
	private String cyclical = "";   //天干地支
	//系统当前时间
	private String sysDate = "";  
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	private int[] mSignDatas=new int[0];
	private ArrayList<Integer> mSignedPosition=new ArrayList<Integer>();
	
	public CalendarAdapter(){
		Date date = new Date();
		sysDate = sdf.format(date);  //当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
		
	}
	
	public CalendarAdapter(Context context,Resources rs,int year, int month, int day){
		this();
		this.context= context;
		sc = new SpecialCalendar();
		//lc = new LunarCalendar();
		this.res = rs;
		currentYear = String.valueOf(year);;  //得到跳转到的年份
		currentMonth = String.valueOf(month);  //得到跳转到的月份
		currentDay = String.valueOf(day);  //得到跳转到的天
		
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
		
	}

	public CalendarAdapter(Context context,Resources rs,String signedDays){
		this();
		this.context= context;
		sc = new SpecialCalendar();
		//lc = new LunarCalendar();
		this.res = rs;

		if(!TextUtils.isEmpty(signedDays)){
			String[] signedDayArry=signedDays.split(",");
			mSignDatas=new int[signedDayArry.length];
			for (int i=0;i<signedDayArry.length;i++){
				mSignDatas[i]=Integer.parseInt(signedDayArry[i]);
			}
		}

		Calendar calendar=Calendar.getInstance();
		currentYear = String.valueOf(calendar.get(Calendar.YEAR)); //得到跳转到的年份
		currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);  //得到跳转到的月份
		currentDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));  //得到跳转到的天

		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
		 }
		TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
		String d = dayNumber[position];
		SpannableString sp = new SpannableString(d);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f) , 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		/*if(dv != null ||dv != ""){
		    sp.setSpan(new RelativeSizeSpan(0.75f), d.length()+1, dayNumber[position].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}*/
//		sp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
		textView.setText(sp);
		textView.setTextColor(Color.GRAY);
		
		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			// 当前月信息显示
			textView.setTextColor(Color.BLACK);// 当月字体设黑
		}
		if(schDateTagFlag != null && schDateTagFlag.length >0){
			for(int i = 0; i < schDateTagFlag.length; i++){
				if(schDateTagFlag[i] == position){
					//设置日程标记背景
					//textView.setBackgroundResource(R.drawable.mark);
				}
			}
		}
		if(currentFlag == position){ 
			//设置当天的背景
			textView.setBackgroundResource(R.drawable.bg_red_empty_circle);
			textView.setTextColor(Color.BLACK);
		}

		for(Integer signedPos:mSignedPosition){
			if(signedPos==position){
				textView.setBackgroundResource(R.drawable.bg_red_circle);
				textView.setTextColor(Color.WHITE);
				break;
			}
		}

		return convertView;
	}
	
	//得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month){
		isLeapyear = sc.isLeapYear(year);              //是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  //某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month);      //某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month-1);  //上一个月的总天数
		Log.d("DAY", isLeapyear+" ======  "+daysOfMonth+"  ============  "+dayOfWeek+"  =========   "+lastDaysOfMonth);
		getWeek(year, month);
	}
	
	//将一个月中的每一天的值添加入数组dayNuber中
	private void getWeek(int year, int month) {
		mSignedPosition.clear();
		int j = 1;
		//得到当前月的所有日程日期(这些日期需要标记)
		for (int i = 0; i < dayNumber.length; i++) {
			 if(i < dayOfWeek){  //前一个月
				int temp = lastDaysOfMonth - dayOfWeek+1;
				dayNumber[i] = String.valueOf((temp + i));
			}else if(i < daysOfMonth + dayOfWeek){   //本月
				int day=i-dayOfWeek+1;//得到的日期
				String dayStr = String.valueOf(day);   //得到的日期
				dayNumber[i] = String.valueOf(day);
				//对于当前月才去标记当前日期
				if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(dayStr)){
					//标记当前日期
					currentFlag = i;
				}	
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));

				 for(int signDay:mSignDatas){
					 if(signDay==day){
						 mSignedPosition.add(i);
						 break;
					 }
				 }
			}else{   //下一个月
				dayNumber[i] = String.valueOf(j);
				j++;
			}
		}
	}
	
	
	public void matchScheduleDate(int year, int month, int day){
		
	}
	
	/**
	 * 点击每一个item时返回item中的日期
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position){
		return dayNumber[position];
	}
	
	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * @return
	 */
	public int getStartPositon(){
		return dayOfWeek+7;
	}
	
	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * @return
	 */
	public int getEndPosition(){
		return  (dayOfWeek+daysOfMonth+7)-1;
	}
	
	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}
	
	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}
	
	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}
	
	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
}