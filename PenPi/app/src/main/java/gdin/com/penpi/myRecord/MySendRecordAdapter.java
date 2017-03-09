package gdin.com.penpi.myRecord;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import gdin.com.penpi.R;
import gdin.com.penpi.activity.PayActivity;
import gdin.com.penpi.commonUtils.FormatUtils;
import gdin.com.penpi.commonUtils.JacksonUtils;
import gdin.com.penpi.domain.Order;

public class MySendRecordAdapter extends RecyclerView.Adapter<MySendRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<Order> mOrderList;

    public MySendRecordAdapter(List<Order> orderList) {
        mOrderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_out_order_recycle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order order = mOrderList.get(position);
        holder.order_id.setText(String.valueOf(order.getOrderID()));
        holder.order_name.setText(order.getSendOrderPeopleName());
        holder.startPlace.setText(order.getStartPlace());
        holder.endPlace.setText(order.getEndPlace());
        holder.telephone.setText(order.getSendOrderPeoplePhone());
        holder.charges.setText(String.valueOf(order.getCharges()));
        holder.Date.setText(FormatUtils.formatTime(order.getSendOrderDate()));

        switch (order.getState()) {
            case "未抢":
                holder.foruse.setEnabled(false);
                holder.foruse.setText("未抢");
                break;
            case "已抢":
                holder.foruse.setEnabled(false);
                holder.foruse.setText("已抢");
                break;
            case "已送达":
                holder.foruse.setEnabled(true);
                holder.foruse.setText("已送达,待付款");
                break;
            case "已付款":
                holder.foruse.setEnabled(true);
                holder.foruse.setText("已付款,待评价");
                break;
            case "已完成":
                holder.foruse.setEnabled(false);
                holder.foruse.setText("已完成");
                break;
        }

        holder.itemView.findViewById(R.id.foruse_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("已送达".equals(order.getState())) {
                    new AlertDialog.Builder(mContext).setTitle("确认付款？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, PayActivity.class);
                                    intent.putExtra("order", JacksonUtils.writeJSON(order));
                                    mContext.startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                } else if ("已付款".equals(order.getState())) {
                    new AlertDialog.Builder(mContext).setTitle("确认评价？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, EvaluationActivity.class);
                                    intent.putExtra("order", JacksonUtils.writeJSON(order));
                                    mContext.startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                }
            }
        });
    }

    /**
     * 返回RecyclerView子项的数目
     * 当用户提交订单信息，Item数目+1，并刷新RecyclerView
     */
    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView order_id;
        TextView order_name;
        TextView startPlace;
        TextView endPlace;
        TextView charges;
        TextView telephone;
        Button foruse;
        TextView Date;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            order_id = (TextView) view.findViewById(R.id.order_people_id);
            order_name = (TextView) view.findViewById(R.id.order_people_name);
            startPlace = (TextView) view.findViewById(R.id.order_start_place);
            endPlace = (TextView) view.findViewById(R.id.order_end_place);
            telephone = (TextView) view.findViewById(R.id.Phonenum);
            charges = (TextView) view.findViewById(R.id.order_charges_name);
            Date = (TextView) view.findViewById(R.id.date_name);
            foruse = (Button) view.findViewById(R.id.foruse_icon);
        }
    }
}