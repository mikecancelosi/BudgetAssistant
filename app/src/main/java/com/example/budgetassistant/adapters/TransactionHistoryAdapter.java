package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Transaction;

import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private Context context;

    public TransactionHistoryAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Description.setText(transactions.get(position).Description);
        holder.Cost.setText("$" + transactions.get(position).Amount );

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,R.array.Categories,R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        holder.Category.setAdapter(spinnerAdapter);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Description;
        TextView Cost;
        Spinner Category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Description = itemView.findViewById(R.id.DescriptionTextView);
            Cost = itemView.findViewById(R.id.AmountTextView);
            Category = itemView.findViewById(R.id.CategorySpinner);

        }
    }

}
