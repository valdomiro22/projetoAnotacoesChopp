package br.com.santos.valdomiro.anotacoeschopp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.santos.valdomiro.anotacoeschopp.EditarActivity;
import br.com.santos.valdomiro.anotacoeschopp.MainActivity;
import br.com.santos.valdomiro.anotacoeschopp.R;
import br.com.santos.valdomiro.anotacoeschopp.controller.DBController;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    View view;
    ViewHolder viewHolder;
    Context context;

    public String[] codigos;
    public String[] horas;
    public static String[] ids;

    public RecyclerViewAdapter(Context context, String[] codigosRecebidos, String[] horasRecebidas, String[] idsRecebidos) {
        this.context = context;
        this.codigos = codigosRecebidos;
        this.horas = horasRecebidas;
        ids = idsRecebidos;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Escopo global
        public TextView textCodigo;
        public TextView textHora;

        @SuppressLint("StaticFieldLeak")
        public static Button buttonEditar;
        @SuppressLint("StaticFieldLeak")
        public static Button buttonDeletar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Escopo ViewHolder
            // Ininialização dos elementos
            textCodigo = itemView.findViewById(R.id.text_codigo);
            textHora = itemView.findViewById(R.id.text_hora);

            buttonEditar = itemView.findViewById(R.id.btn_editar);
            buttonDeletar = itemView.findViewById(R.id.btn_deletar);

            buttonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Troca de Actyvity
                    Intent intent = new Intent(itemView.getContext(), EditarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    itemView.getContext().startActivity(intent);
                }
            });

            buttonDeletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DBController dbController = new DBController(v.getContext());

                    dbController.deletarController(Integer.parseInt(ids[getAdapterPosition()]));

                    MainActivity.preencherListView();
                    MainActivity.preencherQuantidadeDeBarris();
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.recycler_view_itens, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textCodigo.setText(codigos[position]);
        holder.textHora.setText(horas[position]);

        ViewHolder.buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), EditarActivity.class);
                intent2.putExtra("codigo", codigos[position]);
                intent2.putExtra("hora", horas[position]);
                intent2.putExtra("id", ids[position]);
                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent2);

            }
        });

    }

    @Override
    public int getItemCount() {
        return codigos.length;
    }
}
