package com.uitraci.hotel.hotel;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uitraci.hotel.parse.ParseClient;
import com.uitraci.hotel.parse.ParseDTOofClient;
import com.uitraci.hotel.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uitraci.hotel.constant.Action;
import com.uitraci.hotel.dto.FloorDTO;
import com.uitraci.hotel.dto.StatusDTO;
import com.uitraci.hotel.dto.TypeDTO;

/**
 * 选房界面
 *
 * @author 谢波
 *         create by 2016/3/19 0019 10:32
 *         update by 2016/3/26 11:52
 */
public class SelectRoomActivity extends AppCompatActivity {


    private GridView selectroom_gv;
    //记录房间数量，二维数组中第一维表示当前层数，第二维表示当前房间号
    private ArrayList<ArrayList<Integer>> index_room = new ArrayList<ArrayList<Integer>>();
    //房间的标记,在选房时标记用户是否选中该房间
    private ArrayList<ArrayList<Boolean>> flag_isselected = new ArrayList<ArrayList<Boolean>>();
    //房间的标记,标记该房间是否被出售
    private ArrayList<ArrayList<Boolean>> flag_isselled = new ArrayList<ArrayList<Boolean>>();
    //房型
    private ArrayList<ArrayList<Integer>> list_type_room = new ArrayList<ArrayList<Integer>>();
    //对应房间的价格
    private ArrayList<ArrayList<Integer>> list_room_price = new ArrayList<ArrayList<Integer>>();
    //GridView中每一个房间对应一个图片
    private ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    //按下筛选按钮之后弹出的对话框
    private AlertDialog dialog;

    //存储的是楼层的String
    private ArrayList<String> list_floor = new ArrayList<String>();
    //房型的字符串数组,存储所有房型
    private ArrayList<String> list_roomtype = new ArrayList<String>();

    //-----------定义一些flag------------------
    //记录当前是第几层
    private int now_floot = 0;
    //Intent带过来的消息，用来判断是从哪一个Activity跳转过来的
    private String fromWhere;
    //----------------------------------------

    //----------------解析数据用到的---------
    private static Utils utils = new Utils();
    private static String signature;
    private static String token = "cVko8367";
    private static TypeDTO room_typeDTO;
    private static FloorDTO floorDTO;
    private static StatusDTO statusDTO;
    private static ParseClient parseClient = new ParseClient();
    private static ParseDTOofClient parseDTOofClient = new ParseDTOofClient();
    private int selected_room_index;//保存的是选择的房间号在list中的下标
    //-------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_select_room);

        InitView();

        //初始化二维数组 index_room
        init_index_room();

        //初始化标记数组,用来标记对应房间是否被选中 flag_isselected
        init_flag_isselected();

        //初始化标记数组,用来标记对应房间是否出售 flag_isselled
        init_flag_isselled();

        //初始化房型数组
        init_type_room();

        //初始化房间的价格
        init_room_price();

        //更新当前楼层的房间状态
        init_roomstate_nowfloat();

        //将房间显示
        Show_Room_State();
    }


    //初始化控件
    private void InitView() {
        fromWhere = getIntent().getStringExtra("FromWhere");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //-------------处理一些向服务器发送的请求----------------
        requestRoomType();          //请求房型数组，并且初始化list_roomtype
        requestFloot();             //请求楼层数组,并且初始化list_floor
        //---------------------------------------


        selectroom_gv = (GridView) findViewById(R.id.selectroom_gv);
        setTitle("选择入住房型");
        //退出按钮
        findViewById(R.id.selectroom_btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //确认信息按钮
        findViewById(R.id.selectroom_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到付款界面,并根据会员和非会员的身份不同跳转到不同的页面
                //这里应该要分从会员入住跳转过来和从散客入住跳转过来，要传给付款界面一些信息
                //1.传输数据到付款界面，要分为会员和非会员两种情况
                if (fromWhere.equals("member_checkin")) {                      //这是从会员入住跳转过来的
                    if (get_RoomSelected_Index() > 0) {
                        Intent intent = new Intent(SelectRoomActivity.this, PayActivity.class);

                        String enterTime = getIntent().getStringExtra("enterTime");
                        String exitTime = getIntent().getStringExtra("exitTime");
                        int selected_roomindex = get_RoomSelected_Index();
                        int selected_floor = now_floot;
                        intent.putExtra("FromWhere", "member_checkin");
                        intent.putExtra("enterTime", enterTime);
                        intent.putExtra("exitTime", exitTime);
                        intent.putExtra("selected_floor", selected_floor);
                        intent.putExtra("selected_roomindex", selected_roomindex);
                        intent.putExtra("room_price", list_room_price.get(now_floot).get(selected_room_index));

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SelectRoomActivity.this, "请选择一个房间进行入住操作", Toast.LENGTH_SHORT).show();
                    }

                } else if (fromWhere.equals("tourist_checkin")) {                //这是从散客入住跳转过来的
                    if (get_RoomSelected_Index() > 0) {
                        Intent intent = new Intent(SelectRoomActivity.this, TouristCheckinActivity.class);

                        String enterTime = getIntent().getStringExtra("enterTime");
                        String exitTime = getIntent().getStringExtra("exitTime");
                        int selected_roomindex = get_RoomSelected_Index();
                        int selected_floor = now_floot;
                        intent.putExtra("FromWhere", "tourist_checkin");
                        intent.putExtra("enterTime", enterTime);
                        intent.putExtra("exitTime", exitTime);
                        intent.putExtra("selected_floor", selected_floor);
                        intent.putExtra("selected_roomindex", selected_roomindex);
                        intent.putExtra("room_price", list_room_price.get(selected_floor).get(selected_room_index));

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SelectRoomActivity.this, "请选择一个房间进行入住操作", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //筛选按钮
        findViewById(R.id.selectroom_btn_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新建一个对话框，设置筛选的楼层和房间类型
                showSelectDialog();
            }
        });

        //添加GridView的消息处理
        selectroom_gv.setOnItemClickListener(new ItemClickListener());
        //隐藏一些控件
        if (fromWhere.equals("selectroom")) {
            findViewById(R.id.selectroom_btn_confirm).setVisibility(View.GONE);
            TextView selectroom_tv_tip = (TextView) findViewById(R.id.selectroom_tv_tip);
            selectroom_tv_tip.setText("若有疑问请及时咨询前台");
            setTitle("当前房间状态");
        }
    }

    //处理actionbar上面的返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //弹出筛选对话框
    private void showSelectDialog() {
        //保存用户设置的入住事件和离店时间

        AlertDialog.Builder builder = new AlertDialog.Builder(SelectRoomActivity.this);
        //加载自定义的布局文件
        View view = View.inflate(SelectRoomActivity.this, R.layout.dialog_in_select_room, null);

        //选择楼层的Spinner
        Spinner dialog_selectroom_sp_float = (Spinner) view.findViewById(R.id.dialog_selectroom_sp_float);
        ArrayAdapter adapter_float = new ArrayAdapter(SelectRoomActivity.this, android.R.layout.simple_spinner_dropdown_item, list_floor);
        dialog_selectroom_sp_float.setAdapter(adapter_float);
        dialog_selectroom_sp_float.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                now_floot = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //选择房型的Spinner
        Spinner dialog_selectroom_sp_roomtype = (Spinner) view.findViewById(R.id.dialog_selectroom_sp_roomtype);
        ArrayAdapter adapter_roomtype = new ArrayAdapter(SelectRoomActivity.this, android.R.layout.simple_spinner_dropdown_item, list_roomtype);
        dialog_selectroom_sp_roomtype.setAdapter(adapter_roomtype);
        dialog_selectroom_sp_roomtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                flag_room_type_in_selecte = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //取消按钮
        view.findViewById(R.id.dialog_selectroom_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //确认按钮
        view.findViewById(R.id.dialog_selectroom_btn_confirm).setOnClickListener(new View.OnClickListener() {
            /**
             * TODO:这里要添加一些对“确认”按钮按下之后的操作
             * 1.根据所选的楼层和房型更新GridView
             */
            @Override
            public void onClick(View v) {
                //对楼层筛选做出更新
                //对房型筛选做出更新
                update_roomstate_and_type_nowfloat();

                Show_Room_State();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    //在筛选对话框点击确认时通过这个函数来更新当前楼层房间的信息
    private void update_roomstate_and_type_nowfloat(){
        //这里处理显示的房间的状态图,修改map中put的内容
        lstImageItem.clear();
        for (int i = 0; i < index_room.get(now_floot).size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            //加载用户筛选的房型的房间
//            if (list_type_room.get(now_floot).get(i) == flag_room_type_in_selecte){
                if (flag_isselected.get(now_floot).get(i) == true)  //当这个房间被选中时，只需要判断是哪一种房型
                {
                    if (list_type_room.get(now_floot).get(i) <= 3)
                    {
                        map.put("ItemImage", R.mipmap.single_bedroom_selected);
                    }else
                    {
                        map.put("ItemImage", R.mipmap.double_bedroom_selected);
                    }
                }else if (flag_isselected.get(now_floot).get(i) == false &&  flag_isselled.get(now_floot).get(i) == true){
                    if (list_type_room.get(now_floot).get(i) <= 3)
                    {
                        map.put("ItemImage", R.mipmap.single_bedroom_selled);
                    }else
                    {
                        map.put("ItemImage", R.mipmap.double_bedroom_selled);
                    }
                }else if (flag_isselected.get(now_floot).get(i) == false && flag_isselled.get(now_floot).get(i) == false){
                    if (list_type_room.get(now_floot).get(i) <= 3)
                    {
                        map.put("ItemImage", R.mipmap.single_bedroom_normal);
                    }else
                    {
                        map.put("ItemImage", R.mipmap.double_bedroom_normal);
                    }
                }
                map.put("ItemText", String.valueOf(index_room.get(now_floot).get(i)));//按序号做ItemText
                lstImageItem.add(i, map);
            }
//        }
    }

    //初始化flag_isselected数组
    private void init_flag_isselected() {
        for (int i = 0; i < index_room.size(); i++) {
            ArrayList<Boolean> temp_list = new ArrayList<Boolean>();
            for (int j = 0; j < index_room.get(i).size(); j++) {
                temp_list.add(j, false);
            }
            flag_isselected.add(i, temp_list);
        }
    }

    //初始化index_room数组
    private void init_index_room() {
        //TODO:这里将从数据库中读到的房间数量赋值到index_room这个二维数组中
        //测试数据，初始化index_room数组
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> temp_list = new ArrayList<Integer>();
            for (int j = 0; j < 7; j++) {
                temp_list.add(100 * (i + 3) + (j + 1));
            }
            index_room.add(temp_list);
        }
    }

    //初始化flag_isselled数组,房间状态是否被出售
    private void init_flag_isselled() {
        for (int i = 0; i < index_room.size(); i++) {
            ArrayList<Boolean> temp_list = new ArrayList<Boolean>();
            for (int j = 0; j < index_room.get(i).size(); j++) {
                temp_list.add(j, false);
            }
            flag_isselled.add(i, temp_list);
        }
    }

    //初始化房型数组
    private void init_type_room() {
        int count = 0;
        for (int i = 0; i < index_room.size(); i++)
        {
            count = 0;
            ArrayList<Integer> temp_list = new ArrayList<Integer>();
            for (int j = 0; j < index_room.get(i).size(); j++)
            {
                temp_list.add(j, count);
                count++;
            }
            list_type_room.add(i, temp_list);
        }
    }

    //初始化房间价格
    private void init_room_price() {

        for (int i = 0; i < index_room.size(); i++)
        {
            ArrayList<Integer> temp_list = new ArrayList<Integer>();
            temp_list.add(198);
            temp_list.add(398);
            temp_list.add(998);
            temp_list.add(200);
            temp_list.add(498);
            temp_list.add(998);
            temp_list.add(1998);
            list_room_price.add(i, temp_list);
        }
    }

    // 刚开始进入选房界面时，初始化房间状态
    private void init_roomstate_nowfloat()
    {
        //将数据转入ArrayList lstImageItem中
        //这里处理显示的房间的状态图,修改map中put的内容
        for (int i = 0; i < index_room.get(now_floot).size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            //判断当前房间是否是已出售状态，然后判断是什么房型（大床房是一张床图标，其余的是两张床图标）
            if (flag_isselled.get(now_floot).get(i) == true)
            {
                if (list_type_room.get(now_floot).get(i) <= 3)
                {
                    map.put("ItemImage", R.mipmap.single_bedroom_selled);
                }else
                {
                    map.put("ItemImage", R.mipmap.double_bedroom_selled);
                }
            }else if (flag_isselled.get(now_floot).get(i) == false)
            {
                if (list_type_room.get(now_floot).get(i) <= 3)
                {
                    map.put("ItemImage", R.mipmap.single_bedroom_normal);
                }else
                {
                    map.put("ItemImage", R.mipmap.double_bedroom_normal);
                }
            }
            map.put("ItemText", String.valueOf(index_room.get(now_floot).get(i)));   //按序号做ItemText

            lstImageItem.add(i, map);
        }
    }

    //更新当前楼层房间的状态
    private void update_roomstate_nowfloat()
    {
        //这里处理显示的房间的状态图,修改map中put的内容
        for (int i = 0; i < index_room.get(now_floot).size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (flag_isselected.get(now_floot).get(i) == true)  //当这个房间被选中时，只需要判断是哪一种房型
            {
                if (list_type_room.get(now_floot).get(i) <= 3)
                {
                    map.put("ItemImage", R.mipmap.single_bedroom_selected);
                }else
                {
                    map.put("ItemImage", R.mipmap.double_bedroom_selected);
                }
            }else if (flag_isselected.get(now_floot).get(i) == false &&  flag_isselled.get(now_floot).get(i) == true){
                if (list_type_room.get(now_floot).get(i) <= 3)
                {
                    map.put("ItemImage", R.mipmap.single_bedroom_selled);
                }else
                {
                    map.put("ItemImage", R.mipmap.double_bedroom_selled);
                }
            }else if (flag_isselected.get(now_floot).get(i) == false && flag_isselled.get(now_floot).get(i) == false){
                if (list_type_room.get(now_floot).get(i) <= 3)
                {
                    map.put("ItemImage", R.mipmap.single_bedroom_normal);
                }else
                {
                    map.put("ItemImage", R.mipmap.double_bedroom_normal);
                }
            }
            map.put("ItemText", String.valueOf(index_room.get(now_floot).get(i)));//按序号做ItemText
            lstImageItem.set(i, map);
        }
    }

    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //其中position == id
            HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
            selected_room_index = position;
            //点击了房间的图标之后，更新lstImageItem中的数据，显示该房间为选中状态或未选中状态
            //在点击时判断当前房间用户是否已经选中，若选中则要变为非选中状态
            if (flag_isselected.get(now_floot).get(position) == false)
            {
                if (flag_isselled.get(now_floot).get(position) == true)
                {
                    //若是这个房间已售出，则点击后不变化，因此这里什么都不需要写
                }else if (flag_isselled.get(now_floot).get(position) == false)
                {
                    item.remove("ItemImage");
                    if (list_type_room.get(now_floot).get(position) <= 3)
                    {
                        item.put("ItemImage", R.mipmap.single_bedroom_selected);
                    }else
                    {
                        item.put("ItemImage", R.mipmap.double_bedroom_selected);
                    }
                    lstImageItem.set(position, item);
                    flag_isselected.get(now_floot).set(position, true);
                }
            }else if (flag_isselected.get(now_floot).get(position) == true)
            {
                item.remove("ItemImage");
                if (list_type_room.get(now_floot).get(position) <= 3)
                {
                    item.put("ItemImage", R.mipmap.single_bedroom_normal);
                }else
                {
                    item.put("ItemImage", R.mipmap.double_bedroom_normal);
                }
                lstImageItem.set(position, item);
                flag_isselected.get(now_floot).set(position, false);
            }
            //更新好了ArrayList之后更新房间状态,只有在当前点击的房间状态不是售出的房间的情况下，才会清除其他房间的已选择标记。
            if (flag_isselled.get(now_floot).get(position) == false) {
                clear_flag_isselected(position);
                update_roomstate_nowfloat();
                Show_Room_State();
            }else{
                Show_Room_State();
            }
        }
    }

    //得到被选的房间的下标,成功找到返回的是房间号，未成功找到返回的是-1
    private int get_RoomSelected_Index()
    {
        for(int i = 0; i < flag_isselected.size(); i++)
        {
            for(int j = 0; j < flag_isselected.get(i).size(); j++)
            {
                if (flag_isselected.get(i).get(j) == true)
                {
                    return index_room.get(i).get(j);
                }
            }
        }
        return -1;
    }

    //清空除了当前所选房间的外的其他房间的状态
    private void clear_flag_isselected(int index)
    {
        for(int i = 0; i < flag_isselected.size(); i++)
        {
            for(int j = 0; j < flag_isselected.get(i).size(); j++)
            {
                if (i == now_floot && j == index)
                {

                }else{
                    flag_isselected.get(i).set(j,false);
                }
            }
        }
    }

    private void Show_Room_State() {
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(SelectRoomActivity.this,
                lstImageItem,//数据来源
                R.layout.gridview_room_item,//gridview_room_item的XML实现
                new String[]{"ItemImage", "ItemText"},//动态数组与ImageItem对应的子项
                new int[]{R.id.roomitem_iv, R.id.roomitem_tv_text}//ImageItem的XML文件里面的一个ImageView,两个TextView ID
        );
        //添加并且显示
        selectroom_gv.setAdapter(saImageItems);
    }

    //解析得到房型信息
    public void requestRoomType(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String random = utils.RandomGen();
                signature = utils.SHA1(utils.DictOrder(random, token));
                room_typeDTO = parseClient.ParseType(random, signature, Action.QUERY_TYPE, "666666");
                if (room_typeDTO.getResult() == 0){
                    List<TypeDTO.Type> list_room_type = parseDTOofClient.ParseDTOofType(room_typeDTO);

                    for(TypeDTO.Type type : list_room_type){
                        String typename = type.getName();
                        //list_roomtype为一个Arraylist<String>存储所有房型
                        list_roomtype.add(typename);
                    }

                }else{
                    Toast.makeText(SelectRoomActivity.this,"房型信息解析失败",Toast.LENGTH_LONG).show();
                }
            }
        });
        t.start();
    }

    //解析得到楼层信息
    public void requestFloot(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String random = utils.RandomGen();
                signature = utils.SHA1(utils.DictOrder(random, token));
                floorDTO = parseClient.ParseFloor(random, signature, Action.QUERY_FLOOR, "666666");
                if (floorDTO.getResult() == 0){
                    List<FloorDTO.Floor> temp_list_floor = parseDTOofClient.ParseDTOofFloor(floorDTO);

                    for(FloorDTO.Floor floor : temp_list_floor){
                        String floorname = floor.getName();
                        //list_roomtype为一个Arraylist<String>存储所有房型
                        list_floor.add(floorname);
                    }

                }else{
                    Toast.makeText(SelectRoomActivity.this,"楼层信息解析失败",Toast.LENGTH_LONG).show();
                }
            }
        });
        t.start();
    }

}

