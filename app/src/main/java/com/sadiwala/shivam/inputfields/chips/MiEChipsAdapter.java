package com.sadiwala.shivam.inputfields.chips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.chips.MiEChipCodeValueModel;
import com.sadiwala.shivam.models.chips.MiEChipModel;
import com.sadiwala.shivam.models.common.CodeValue;
import com.sadiwala.shivam.util.CustomTextView;

import java.util.List;

public class MiEChipsAdapter extends RecyclerView.Adapter<MiEChipsAdapter.ViewHolder> {
    public interface OnChipItemClickListener {
        void onItemClick(CodeValue item, int position);
    }

    private static final String TAG = MiEChipsAdapter.class.getSimpleName();
    private MiEChipModel mieChipModel;
    private final Context context;
    private OnChipItemClickListener listener;

    public MiEChipsAdapter(MiEChipModel mieChipModel, Context context, OnChipItemClickListener listener) {
        this.mieChipModel = mieChipModel;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_single_mie_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<CodeValue> chipsList = ((MiEChipCodeValueModel) mieChipModel).getChips();
        CodeValue chip = chipsList.get(position);
        if (chip != null) {
            holder.bindItem(chip, listener);
        }
    }

    @Override
    public int getItemCount() {
        List<CodeValue> chipsList = ((MiEChipCodeValueModel) mieChipModel).getChips();
        return chipsList.size();
    }

    public void setListener(OnChipItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(MiEChipModel mieChipModel) {
        this.mieChipModel = mieChipModel;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CustomTextView tvText;
        private final ImageView ivLeftDrawable;
        private final LinearLayout llSingleMiEChip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.ctv_single_mie_chip);
            ivLeftDrawable = itemView.findViewById(R.id.iv_single_mie_chip_left_drawable);
            llSingleMiEChip = itemView.findViewById(R.id.ll_single_mie_chip);
        }

        public void bindItem(final CodeValue chip, final OnChipItemClickListener listener) {
            tvText.setText(chip.getValue());
            itemView.setOnClickListener(v -> listener.onItemClick(chip, getAdapterPosition()));
        }
    }
}
