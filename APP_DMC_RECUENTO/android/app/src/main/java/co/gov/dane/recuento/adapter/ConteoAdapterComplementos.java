package co.gov.dane.recuento.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.gov.dane.recuento.Formulario;
import co.gov.dane.recuento.FormularioEdificacion;
import co.gov.dane.recuento.FormularioUnidad;
import co.gov.dane.recuento.R;
import co.gov.dane.recuento.dtos.ComplementoNormalizadorDTO;
import co.gov.dane.recuento.model.ConteoEdificacion;

public class ConteoAdapterComplementos extends RecyclerView.Adapter<ConteoAdapterComplementos.MyViewHolder> implements
        ItemTouchHelperAdapter {

    private List<ComplementoNormalizadorDTO> complemento;
    private FormularioUnidad form;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(complemento, fromPosition, toPosition);
        ComplementoNormalizadorDTO targetUser = complemento.get(fromPosition);
        complemento.remove(fromPosition);
        complemento.add(toPosition, targetUser);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public TextView id, tvTexComplemento, tvComplemento;
        public ImageView eliminar_item, editar_item;
        public LinearLayout contenedor;
        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.idNormalizador);
            tvComplemento = (TextView) view.findViewById(R.id.direcComp);
            tvTexComplemento = (TextView) view.findViewById(R.id.direcTextComp);
            eliminar_item = view.findViewById(R.id.eliminar_item_complemento);
            editar_item = view.findViewById(R.id.editar_item_complemento);
            contenedor = (LinearLayout) view.findViewById(R.id.contenedorComplemento);

        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }

    public ConteoAdapterComplementos(List<ComplementoNormalizadorDTO> moviesList, FormularioUnidad form) {
        this.complemento = moviesList;
        this.form=form;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complemento_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ComplementoNormalizadorDTO comple = complemento.get(position);
        holder.id.setText(comple.getId());
        if(comple.getId()== null){
            holder.editar_item.setVisibility(View.GONE);
        }else{
            holder.editar_item.setVisibility(View.VISIBLE);
        }
        holder.tvComplemento.setText(comple.getDirecComp());
        holder.tvTexComplemento.setText(comple.getDirecTextComp());
        holder.eliminar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form.showDialogBorrarComplemneto(position);
            }
        });

        holder.editar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form.showDialogEditarComplemneto(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return complemento.size();
    }


}