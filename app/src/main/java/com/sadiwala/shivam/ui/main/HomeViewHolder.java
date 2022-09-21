package com.sadiwala.shivam.ui.main;

import static com.sadiwala.shivam.ui.main.HomeRecycleviewAdapter.REPORT_TYPE_THIS_MONTH;
import static com.sadiwala.shivam.ui.main.HomeRecycleviewAdapter.REPORT_TYPE_THIS_WEEK;
import static com.sadiwala.shivam.ui.main.HomeRecycleviewAdapter.REPORT_TYPE_TODAY;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.Customer;
import com.sadiwala.shivam.models.Order;
import com.sadiwala.shivam.models.Report;
import com.sadiwala.shivam.network.FirebaseDatabaseController;
import com.sadiwala.shivam.util.AaryaDateFormats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout container;
    private TextView groupTitle, orderValue, totalCustomersValue, todaysCustomersValue;
    private Report report;
    private Activity mActivity;
    private Calendar start = Calendar.getInstance(), end = Calendar.getInstance();

    public HomeViewHolder(View itemView) {
        super(itemView);
        groupTitle = itemView.findViewById(R.id.groupTitle);
        orderValue = itemView.findViewById(R.id.orderValue);
        totalCustomersValue = itemView.findViewById(R.id.totalCustomersValue);
        todaysCustomersValue = itemView.findViewById(R.id.todaysCustomersValue);
        container = itemView.findViewById(R.id.container);
    }

    public void setData(Report report, Activity mActivity) {
        this.report = report;
        this.mActivity = mActivity;
        calculateData();
    }

    private void calculateData() {
        if (REPORT_TYPE_TODAY.equals(report.getType())) {
            container.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.aline_gown_bg_color));
            groupTitle.setText(mActivity.getString(R.string.today));

            start = Calendar.getInstance();
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);

            end = Calendar.getInstance();
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            end.set(Calendar.MILLISECOND, 0);

        } else if (REPORT_TYPE_THIS_WEEK.equals(report.getType())) {
            container.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.chapati_gown_bg_color));

            start = Calendar.getInstance();
            start.set(DAY_OF_WEEK, start.getFirstDayOfWeek() + 1); // Adding extra 1, so week starts from MONDAY instead of SUNDAY
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);

            end = Calendar.getInstance();
            end.setTime(new Date(start.getTimeInMillis()));
            end.add(Calendar.DATE, 6);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            end.set(Calendar.MILLISECOND, 0);

            String range = AaryaDateFormats.getFormatddMMM().format(new Date(start.getTimeInMillis())) + "-" + AaryaDateFormats.getFormatddMMM().format(new Date(end.getTimeInMillis()));
            groupTitle.setText(mActivity.getString(R.string.this_week) + " ( " + range + " )");

        } else if (REPORT_TYPE_THIS_MONTH.equals(report.getType())) {
            container.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.night_shoot_bg_color));

            start = Calendar.getInstance();
            start.set(DAY_OF_MONTH, 1);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);

            int totalDaysInMonth = start.getActualMaximum(DAY_OF_MONTH);
            end = Calendar.getInstance();
            end.setTime(new Date(start.getTimeInMillis()));
            end.add(Calendar.DATE, totalDaysInMonth - 1);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);
            end.set(Calendar.MILLISECOND, 0);

            String range = AaryaDateFormats.getFormatddMMM().format(new Date(start.getTimeInMillis())) + "-" + AaryaDateFormats.getFormatddMMM().format(new Date(end.getTimeInMillis()));
            groupTitle.setText(mActivity.getString(R.string.this_month) + " ( " + range + " )");
        }

        ArrayList<Order> orders = FirebaseDatabaseController.getOrdersInRange(start.getTimeInMillis(), end.getTimeInMillis());
        orderValue.setText("" + orders.size());

        int totalCustomers = FirebaseDatabaseController.getUniqueCustomersCountFromOrders(orders);
        totalCustomersValue.setText("" + totalCustomers);

        ArrayList<Customer> customers = FirebaseDatabaseController.getCustomersInRange(start.getTimeInMillis(), end.getTimeInMillis());
        todaysCustomersValue.setText("" + customers.size());

    }


}