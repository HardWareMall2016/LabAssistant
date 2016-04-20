package net.oschina.app.v2.activity.settings.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

/**
 * 主界面
 * 
 * @author zihao 2013-11-12
 * 
 */
public class MainActivity extends Activity {

	// 这个数组是用来存储一级item的点击次数的，根据点击次数设置一级标签的选中、为选中状态

	// 用来标识是否设置二級item背景色为绿色,初始值为-1既为选中状态
	private int child_groupId = -1;
	private int child_childId = -1;
	
	// 这个数组是用来存储一级item的点击次数的，根据点击次数设置一级标签的选中、为选中状态
		private int[] group_checked = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		private String[] group_title_arry = new String[] { "颈椎测试", "腰部测试" };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v2_fragment_help_new);
		ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.list);
		// 设置默认图标为不显示状态
		expandableListView.setGroupIndicator(null);
		// 为列表绑定数据源
		expandableListView.setAdapter(adapter);
		// 设置一级item点击的监听器
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				group_checked[groupPosition] = group_checked[groupPosition] + 1;
				// 刷新界面
				((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
				return false;
			}
		});

		group_title_arry=getResources().getStringArray(R.array.help_levelOne);
		/*// 设置二级item点击的监听器
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// 将被点击的一丶二级标签的位置记录下来
				child_groupId = groupPosition;
				child_childId = childPosition;
				// 刷新界面
				((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
				return false;
			}
		});*/
	}

	final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
		// 一级标签上的logo图片数据源
		// 一级标签上的标题数据源
		
		// 子视图显示文字
		private String[][] child_text_array = new String[][] {
				{ "实验助手是一群志同道合的检测分析相关从业人员共同建立的，专注于实验室检测行业的问答、技术交流平台。实验室检测从业人员们会把自己在工作、生活、学习中遇到的问题发布到实验助手，有相关知识、经验、专业技能的网友们会来回答问题。同时，这些问题的答案又会进一步作为搜索结果，提供给其他有类似疑问的用户，达到分享知识的效果。"},
				{ "首页-我的实验圈在用户登录后，会显示我所关注的对象的提问、回答，以及我的提问、回答等操作。" },
				{ "提问、回答、获得悬赏积分、参与问卷调查、邀请好友加入、评论文章等都会获得相应的积分。积分可在积分商城换取相应的礼品。" },
				{ "为了营造良好的学习和讨论环境，以下内容将无法通过审核：", "1、内容包含色情、暴力、辱骂等方面的敏感词", "2、提问内容涉及明显的广告行为", "3、问题不清晰、不完整、没有任何意义的问题" },
				{"通过非正常手段来刷积分的用户，我们会严厉打击，发现者直接封号处理永不解冻"},
				{"被举报的问题或回答，在工作人员审核属实后，第一次会提醒用户。如果同一人多次被举报属实，则直接封停账号永不解冻"},
				{"积分商城分为实物礼品和学习资料，用户可根据需求自行选择需要的商品","1、实物礼品：用户提交兑换申请后，填写详细的寄送信息。一般1-2个工作日审核后即会发货，发货信息会由短信通知。","2、学习资料：用户提交兑换申请后，填写能接受较大附件的邮箱，系统会自动发送资料到用户提供的邮箱。如果在1-2天都未收到。请查看垃圾邮箱或联系客服QQ 2076938360"},
				{"如有任何问题或建议，欢迎来电交流！","客服电话：021-62964801 周一~周五  9:30- 17:00","客服QQ：2076938360  周一~周五  9:30- 17:00","创始人QQ：13818255349"},
				{"微信公众号：shiyanzhushouapp （实验助手app 拼音）","创始人微信：syzstutuzi","新浪微博：@shiyanzhushou"}};
		// 一级标签上的状态图片数据源
		int[] group_state_array = new int[] { R.drawable.group_down,
				R.drawable.group_up };

		// 重写ExpandableListAdapter中的各个方法
		/**
		 * 获取一级标签总数
		 */
		@Override
		public int getGroupCount() {
			return group_title_arry.length;
		}

		/**
		 * 获取一级标签内容
		 */
		@Override
		public Object getGroup(int groupPosition) {
			return group_title_arry[groupPosition];
		}

		/**
		 * 获取一级标签的ID
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		/**
		 * 获取一级标签下二级标签的总数
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return child_text_array[groupPosition].length;
		}

		/**
		 * 获取一级标签下二级标签的内容
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return child_text_array[groupPosition][childPosition];
		}

		/**
		 * 获取二级标签的ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		/**
		 * 指定位置相应的组视图
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * 对一级标签进行设置
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// 为视图对象指定布局
			convertView = (LinearLayout) LinearLayout.inflate(getBaseContext(),
					R.layout.group_layout, null);

			RelativeLayout myLayout = (RelativeLayout) convertView
					.findViewById(R.id.group_layout);

			/**
			 * 声明视图上要显示的控件
			 */
			// 新建一个TextView对象，用来显示一级标签上的标题信息
			TextView group_title = (TextView) convertView
					.findViewById(R.id.group_title);
			// 新建一个TextView对象，用来显示一级标签上的大体描述的信息
			ImageView group_state = (ImageView) convertView
					.findViewById(R.id.group_state);
			/**
			 * 设置相应控件的内容
			 */
			// 设置标题上的文本信息
			group_title.setText(group_title_arry[groupPosition]);
			// 设置整体描述上的文本信息

			if (group_checked[groupPosition] % 2 == 1) {
				// 设置默认的图片是选中状态
				group_state.setBackgroundResource(group_state_array[1]);
				myLayout.setBackgroundResource(R.drawable.text_item_top_bg);
			} else {
				for (int test : group_checked) {
					if (test == 0 || test % 2 == 0) {
						myLayout.setBackgroundResource(R.drawable.text_item_bg);
						// 设置默认的图片是未选中状态
						group_state.setBackgroundResource(group_state_array[0]);
					}
				}
			}
			// 返回一个布局对象
			return convertView;
		}

		/**
		 * 对一级标签下的二级标签进行设置
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			// 为视图对象指定布局
			convertView = (RelativeLayout) RelativeLayout.inflate(
					getBaseContext(), R.layout.child_layout, null);
			/**
			 * 声明视图上要显示的控件
			 */
			// 新建一个TextView对象，用来显示具体内容
			TextView child_text = (TextView) convertView
					.findViewById(R.id.child_text);
			/**
			 * 设置相应控件的内容
			 */
			// 设置要显示的文本信息
			child_text.setText(child_text_array[groupPosition][childPosition]);
			// 判断item的位置是否相同，如相同，则表示为选中状态，更改其背景颜色，如不相同，则设置背景色为白色
			if (child_groupId == groupPosition
					&& child_childId == childPosition) {
				// 设置背景色为绿色
				// convertView.setBackgroundColor(Color.GREEN);
			} else {
				// 设置背景色为白色
				// convertView.setBackgroundColor(Color.WHITE);
			}
			// 返回一个布局对象
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}


	};

}