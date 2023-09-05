package it.unimib.cookery.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.cookery.R;

import it.unimib.cookery.models.StepApi;

public class RecipeProcedureAdapter extends RecyclerView.Adapter<RecipeProcedureAdapter.StepViewHolder> {

    private List<String> mListStep;

    public void setData(ArrayList<String> list) {
        this.mListStep = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step_recipe, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {

            holder.tvNStep.setText("Step " + (position + 1) + ": ");
            holder.tvDescription.setText(mListStep.get(position));

    }

    @Override
    public int getItemCount() {
        if (mListStep != null) {
            return mListStep.size();
        }
        return 0;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNStep;
        private TextView tvDescription;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNStep = itemView.findViewById(R.id.tv_n_step);
            tvDescription = itemView.findViewById(R.id.tv_descripion);
        }
    }
}
