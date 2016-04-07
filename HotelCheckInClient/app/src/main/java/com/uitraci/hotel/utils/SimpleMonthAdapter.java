/***********************************************************************************
 * The MIT License (MIT)

 * Copyright (c) 2014 Robin Chutaux

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package com.uitraci.hotel.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.uitraci.hotel.hotel.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SimpleMonthAdapter extends RecyclerView.Adapter<SimpleMonthAdapter.ViewHolder> implements SimpleMonthView.OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
	private final Context mContext;
	private final DatePickerController mController;
    private final Calendar calendar;
    private final SelectedDays<CalendarDay> selectedDays;
    private final Integer firstMonth;
    private final Integer lastMonth;
    private int flag_dateisvalidate = 0;

	public SimpleMonthAdapter(Context context, DatePickerController datePickerController, TypedArray typedArray) {
        this.typedArray = typedArray;
        firstMonth = typedArray.getInt(R.styleable.DayPickerView_firstMonth, -1);//月份的索引从0开始
        lastMonth = typedArray.getInt(R.styleable.DayPickerView_lastMonth, -1);
        selectedDays = new SelectedDays<CalendarDay>();
		mContext = context;
		mController = datePickerController;
        calendar = Calendar.getInstance();
		init();
//        Log.e("SimpleMonthAdapter()::firstMonth======", firstMonth + "");
//        Log.e("SimpleMonthAdapter()::lastMonth======", lastMonth + "");
	}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final SimpleMonthView simpleMonthView = new SimpleMonthView(mContext, typedArray);
        return new ViewHolder(simpleMonthView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        int month;
        int year;
        SimpleMonthView v = viewHolder.simpleMonthView;
        HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();

        if (firstMonth != -1)
        {
            month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
            year = position / MONTHS_IN_YEAR + calendar.get(Calendar.YEAR) + ((firstMonth + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);
        }
        else
        {
            month = position % MONTHS_IN_YEAR;
            year = /*position / MONTHS_IN_YEAR +*/ calendar.get(Calendar.YEAR);
        }


        int selectedFirstDay = -1;
        int selectedLastDay = -1;
        int selectedFirstMonth = -1;
        int selectedLastMonth = -1;
        int selectedFirstYear = -1;
        int selectedLastYear = -1;

        if (selectedDays.getFirst() != null)
        {
            selectedFirstDay = selectedDays.getFirst().day;
            selectedFirstMonth = selectedDays.getFirst().month;
            selectedFirstYear = selectedDays.getFirst().year;
        }

        if (selectedDays.getLast() != null)
        {
            selectedLastDay = selectedDays.getLast().day;
            selectedLastMonth = selectedDays.getLast().month;
            selectedLastYear = selectedDays.getLast().year;
        }

        v.reuse();

        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, selectedFirstYear);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_YEAR, selectedLastYear);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, selectedFirstMonth);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_MONTH, selectedLastMonth);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, selectedFirstDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DAY, selectedLastDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
//        Log.e("onBindViewHolder()::selectedFirstYear======", selectedFirstYear + "");
//        Log.e("onBindViewHolder()::selectedLastYear======", selectedLastYear + "");
//        Log.e("onBindViewHolder()::selectedFirstMonth======", selectedFirstMonth + "");
//        Log.e("onBindViewHolder()::selectedLastMonth======", selectedLastMonth + "");
//        Log.e("onBindViewHolder()::selectedFirstDay======", selectedFirstDay + "");
//        Log.e("onBindViewHolder()::selectedLastDay======", selectedLastDay + "");
//        Log.e("onBindViewHolder()::year======", year + "");
//        Log.e("onBindViewHolder()::month======", month + "");
//        Log.e("onBindViewHolder()::calendar.getFirstDayOfWeek()======", calendar.getFirstDayOfWeek() + "");
        v.invalidate();
    }

    public long getItemId(int position) {
		return position;
	}

    @Override
    public int getItemCount()
    {
        int itemCount = (((mController.getMaxYear() - calendar.get(Calendar.YEAR)) + 1) * MONTHS_IN_YEAR);
//        Log.e("getItemCount.itemCount0000======",itemCount+"");
        if (firstMonth != -1)
            itemCount -= firstMonth;
//        Log.e("getItemCount.itemCount1111======",itemCount+"");
        if (lastMonth != -1)
            itemCount -= (MONTHS_IN_YEAR - lastMonth) - 1;
//        Log.e("getItemCount.itemCount2222======",itemCount+"   lastMonth   " + lastMonth);
        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final SimpleMonthView simpleMonthView;

        public ViewHolder(View itemView, SimpleMonthView.OnDayClickListener onDayClickListener)
        {
            super(itemView);
            simpleMonthView = (SimpleMonthView) itemView;
            simpleMonthView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            simpleMonthView.setClickable(true);
            simpleMonthView.setOnDayClickListener(onDayClickListener);
        }
    }

	protected void init() {
        if (typedArray.getBoolean(R.styleable.DayPickerView_currentDaySelected, false))
            onDayTapped(new CalendarDay(System.currentTimeMillis()));
	}

	public void onDayClick(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
		if (calendarDay != null) {
			onDayTapped(calendarDay);
        }
	}

	protected void onDayTapped(CalendarDay calendarDay) {
		setSelectedDay(calendarDay);
        if (selectedDays.getFirst() != null && selectedDays.getLast() != null)
        {
            flag_dateisvalidate = 1;
        }else{
            flag_dateisvalidate = 0;
        }
        mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day, flag_dateisvalidate);
	}

	public void setSelectedDay(CalendarDay calendarDay) {
        if (selectedDays.getFirst() != null && selectedDays.getLast() == null)//已经点了一次，这是第二次点
        {
            selectedDays.setLast(calendarDay);

            if (selectedDays.getFirst().month < calendarDay.month)
            {
                //搞不懂这个是要干嘛
                for (int i = 0; i < selectedDays.getFirst().month - calendarDay.month - 1; ++i)
                {
                    mController.onDayOfMonthSelected(selectedDays.getFirst().year, selectedDays.getFirst().month + i, selectedDays.getFirst().day,flag_dateisvalidate);
                }
            }
            //-----update by xiebo---- 保证了退房日期不能再入住日期之前--------
            if (calendarDay.day < selectedDays.getFirst().day && calendarDay.month == selectedDays.getFirst().month)
            {
                selectedDays.setFirst(calendarDay);
                selectedDays.setLast(null);
            }
            if (calendarDay.month < selectedDays.getFirst().month && calendarDay.year == selectedDays.getFirst().year)
            {
                selectedDays.setFirst(calendarDay);
                selectedDays.setLast(null);
            }
            if ( calendarDay.year < selectedDays.getFirst().year )
            {
                selectedDays.setFirst(calendarDay);
                selectedDays.setLast(null);
            }
            //------------------------------------------------------
        }
        else if (selectedDays.getLast() != null)
        {
            selectedDays.setFirst(calendarDay);
            selectedDays.setLast(null);
        }
        else
            selectedDays.setFirst(calendarDay);

		notifyDataSetChanged();
	}

	public static class CalendarDay implements Serializable
    {
        private static final long serialVersionUID = -5456695978688356202L;
        private Calendar calendar;

		int day;
		int month;
		int year;

		public CalendarDay() {
			setTime(System.currentTimeMillis());
		}

		public CalendarDay(int year, int month, int day) {
			setDay(year, month, day);
		}

		public CalendarDay(long timeInMillis) {
			setTime(timeInMillis);
		}

		public CalendarDay(Calendar calendar) {
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		private void setTime(long timeInMillis) {
			if (calendar == null) {
				calendar = Calendar.getInstance();
            }
			calendar.setTimeInMillis(timeInMillis);
			month = this.calendar.get(Calendar.MONTH);
			year = this.calendar.get(Calendar.YEAR);
			day = this.calendar.get(Calendar.DAY_OF_MONTH);
		}

		public void set(CalendarDay calendarDay) {
		    year = calendarDay.year;
			month = calendarDay.month;
			day = calendarDay.day;
		}

		public void setDay(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
		}

        public Date getDate()
        {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.set(year, month, day);
            return calendar.getTime();
        }
	}

    public SelectedDays<CalendarDay> getSelectedDays()
    {
        return selectedDays;
    }

    public static class SelectedDays<K> implements Serializable
    {
        private static final long serialVersionUID = 3942549765282708376L;
        private K first;
        private K last;

        public K getFirst()
        {
            return first;
        }

        public void setFirst(K first)
        {
            this.first = first;
        }

        public K getLast()
        {
            return last;
        }

        public void setLast(K last)
        {
            this.last = last;
        }
    }
}