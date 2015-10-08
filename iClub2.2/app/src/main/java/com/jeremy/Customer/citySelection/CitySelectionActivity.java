package com.jeremy.Customer.citySelection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremy.Customer.R;
import com.jeremy.Customer.bean.Identification;

import java.util.Arrays;


public class CitySelectionActivity extends Activity implements View.OnClickListener{

    /**
     * Called when the activity is first created.
     */
//	private static ArrayList<ProvinceBean> parentData = new ArrayList<ProvinceBean>();

// private ListView lvContact;
    private SideBar indexBar;
    private WindowManager mWindowManager;
    private TextView mDialogText;
    //    private String PROVINCE_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionProvince";
    String[] cityName;
    //    static int i;
    ListViewAdp lAdp;
    ProgressDialog prodialog;
    ListView lvContact;

    private TextView hot_city1,hot_city2,hot_city3,hot_city4,hot_city5,hot_city6,hot_city7,hot_city8,hot_city9;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);
        lvContact = (ListView) this.findViewById(R.id.lvContact);
        View header = View.inflate(CitySelectionActivity.this, R.layout.hot_city_slot_head, null);//头部内容
        hot_city1 = (TextView)header.findViewById(R.id.hot_city1);
        hot_city2 = (TextView)header.findViewById(R.id.hot_city2);
        hot_city3 = (TextView)header.findViewById(R.id.hot_city3);
        hot_city4 = (TextView)header.findViewById(R.id.hot_city4);
        hot_city5 = (TextView)header.findViewById(R.id.hot_city5);
        hot_city6 = (TextView)header.findViewById(R.id.hot_city6);
        hot_city7 = (TextView)header.findViewById(R.id.hot_city7);
        hot_city8 = (TextView)header.findViewById(R.id.hot_city8);
        hot_city9 = (TextView)header.findViewById(R.id.hot_city9);
        hot_city1.setOnClickListener(this);
        hot_city2.setOnClickListener(this);
        hot_city3.setOnClickListener(this);
        hot_city4.setOnClickListener(this);
        hot_city5.setOnClickListener(this);
        hot_city6.setOnClickListener(this);
        hot_city7.setOnClickListener(this);
        hot_city8.setOnClickListener(this);
        hot_city9.setOnClickListener(this);
        lvContact.addHeaderView(header);//添加头部
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // new Thread(runa).start();
        findView();
        getdata();
        // 设置右侧触摸监听
        indexBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // TODO Auto-generated method stub
                // 该字母首次出现的位置
                int position = lAdp.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lvContact.setSelection(position);
                }
            }
        });

    }

    Handler hand = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // lAdp.notifyDataSetChanged();// 动态更新ListView
                    prodialog.dismiss();
                    lAdp = new ListViewAdp(CitySelectionActivity.this,cityName);
                    lvContact.setAdapter(lAdp);

                    lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TextView contactitem_nick = (TextView)view.findViewById(R.id.contactitem_nick);
                            returnCity(contactitem_nick.getText().toString());
                        }
                    });

                    // findView();
                    String Up[] = lAdp.first;
                    System.out.println("00000000" + Up);
                    break;

                default:
                    break;
            }

        }

    };

    //返回选择的值
    private void returnCity(String nameCity){
        String a[] = CitySelectionActivity.this.getString(R.string.area).split(":"+nameCity)[0].split(",");
        Intent intent = new Intent();
        intent.putExtra("City", Integer.parseInt(a[a.length-1]));
        intent.putExtra("CityName", nameCity);
                        /*给上一个Activity返回结果*/
        CitySelectionActivity.this.setResult(Identification.CITYSELECTION, intent);
                        /*结束本Activity*/
        CitySelectionActivity.this.finish();
    }

    private void findView() {

        // indexBar=new SideBar(this, lAdp.first);
        indexBar = (SideBar) findViewById(R.id.sideBar);
        // indexBar.setListView(lvContact);
        mDialogText = (TextView) LayoutInflater.from(this).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);

        indexBar.setTextView(mDialogText);
    }

    private static String[] sArea;

    public void getdata() {

        System.out.println("1111111112");
        prodialog = ProgressDialog.show(CitySelectionActivity.this, "",
                "正在更新数据……", true, true);
        System.out.println("1111111113");
        Thread thread = new Thread() {

            @Override
            public void run() {
                sArea = CitySelectionActivity.this.getString(R.string.area).split(",");

                cityName = new String[]{"阿坝", "阿克苏", "阿拉尔", "阿拉善", "阿勒泰", "阿里", "安康", "安庆", "鞍山", "安顺", "安阳", "澳门", "阿图什", "白城", "百色", "白沙", "白山", "白银", "蚌埠", "保定", "宝鸡", "保山", "保亭", "包头", "巴彦淖尔", "巴中", "北海", "北京", "本溪", "毕节", "滨州", "博乐", "亳州", "沧州", "常德", "昌都", "昌吉", "昌江", "常州", "巢湖", "朝阳", "潮州", "承德", "成都", "澄迈", "郴州", "赤峰", "池州", "崇左", "楚雄", "滁州", "大理", "大连", "丹东", "儋州", "大庆", "大同", "大兴安岭", "达州", "德宏", "德阳", "德州", "定安", "定西", "迪庆", "东方", "东莞", "东营", "鄂尔多斯", "恩施", "鄂州", "防城", "佛山", "阜康", "抚顺", "阜新", "阜阳", "福州", "抚州", "甘南", "赣州", "甘孜", "高雄", "高雄", "广安", "广元", "广州", "贵港", "桂林", "贵阳", "果洛", "固原", "哈尔滨", "海北", "海东", "海口", "海南", "海西", "哈密", "邯郸", "杭州", "汉中", "鹤壁", "河池", "合肥", "鹤岗", "黑河", "衡水", "衡阳", "和田", "河源", "菏泽", "贺州", "红河", "淮安", "淮北", "怀化", "淮南", "花莲", "黄冈", "黄南", "黄山", "黄石", "呼和浩特", "惠州", "葫芦岛", "呼伦贝尔", "湖州", "佳木斯", "吉安", "江门", "焦作", "嘉兴", "嘉义", "嘉峪关", "揭阳", "吉林", "基隆", "济南", "金昌", "晋城", "景德镇", "荆门", "荆州", "金华", "济宁", "晋中", "锦州", "九江", "酒泉", "鸡西", "济源", "开封", "喀什", "克拉玛依", "库尔勒", "奎屯", "昆明", "来宾", "莱芜", "廊坊", "兰州", "拉萨", "乐东", "乐山", "凉山", "连云港", "聊城", "辽阳", "辽源", "丽江", "临沧", "临汾", "临高", "陵水", "临夏", "临沂", "林芝", "丽水", "六安", "六盘水", "柳州", "陇南", "龙岩", "娄底", "漯河", "洛阳", "泸州", "吕梁", "马鞍山", "茂名", "眉山", "梅州", "绵阳", "苗栗", "米泉", "牡丹江", "南昌", "南充", "南京", "南宁", "南平", "南通", "南投", "南阳", "内江", "那曲", "宁波", "宁德", "怒江", "盘锦", "攀枝花", "澎湖", "平顶山", "屏东", "平凉", "萍乡", "莆田", "濮阳", "黔东", "潜江", "黔南", "黔西", "青岛", "庆阳", "清远", "秦皇岛", "钦州", "琼海", "琼中", "齐齐哈尔", "七台河", "泉州", "曲靖", "衢州", "日喀则", "日照", "三门峡", "三明", "三亚", "厦门", "上海", "商洛", "商丘", "上饶", "山南", "汕头", "汕尾", "韶关", "绍兴", "邵阳", "神农架", "沈阳", "深圳", "石河子", "石家庄", "十堰", "石嘴山", "双鸭山", "朔州", "思茅", "四平", "松原", "绥化", "遂宁", "随州", "宿迁", "苏州", "宿州", "塔城", "泰安", "台北", "台北", "台东", "台南", "台南", "太原", "台中", "台中", "泰州", "台州", "唐山", "桃园", "天津", "天门", "天水", "铁岭", "铜川", "通化", "通辽", "铜陵", "铜仁", "吐鲁番", "图木舒克", "屯昌", "万宁", "潍坊", "威海", "渭南", "文昌", "文山", "温州", "乌海", "武汉", "芜湖", "五家渠", "乌兰察布", "乌鲁木齐", "乌苏", "武威", "无锡", "五指山", "吴忠", "梧州", "西安", "襄樊", "香港", "湘潭", "湘西", "咸宁", "仙桃", "咸阳", "孝感", "锡林郭勒", "兴安", "邢台", "西宁", "新乡", "信阳", "新余", "忻州", "新竹", "新竹", "西双版纳", "宣城", "许昌", "徐州", "雅安", "延安", "延边", "盐城", "阳江", "阳泉", "扬州", "烟台", "宜宾", "宜昌", "伊春", "宜春", "宜兰", "银川", "营口", "鹰潭", "伊宁", "益阳", "永州", "岳阳", "榆林", "玉林", "运城", "云浮", "云林", "玉树", "玉溪", "枣庄", "长春", "彰化", "张家界", "张家口", "长沙", "张掖", "长治", "漳州", "湛江", "肇庆", "昭通", "郑州", "镇江", "重庆", "中山", "中卫", "周口", "舟山", "珠海", "驻马店", "株洲", "淄博", "自贡", "资阳", "遵义"};
//                for (int i = 0; i < sArea.length; i++) {
//                    cityName[i] = sArea[i].split(":")[1];
//                }
                Arrays.sort(cityName, new PinyinComparator());
//				parentData.clear();
//				Domxml.getDomTools().aaa(parentData);
                Message msg = hand.obtainMessage();
                msg.what = 1;
                msg.sendToTarget();
            }
        };
        thread.start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hot_city1:
                returnCity(hot_city1.getText().toString());
                break;
            case R.id.hot_city2:
                returnCity(hot_city2.getText().toString());
                break;
            case R.id.hot_city3:
                returnCity(hot_city3.getText().toString());
                break;
            case R.id.hot_city4:
                returnCity(hot_city4.getText().toString());
                break;
            case R.id.hot_city5:
                returnCity(hot_city5.getText().toString());
                break;
            case R.id.hot_city6:
                returnCity(hot_city6.getText().toString());
                break;
            case R.id.hot_city7:
                returnCity(hot_city7.getText().toString());
                break;
            case R.id.hot_city8:
                returnCity(hot_city8.getText().toString());
                break;
            case R.id.hot_city9:
                returnCity(hot_city9.getText().toString());
                break;

        }
    }
}