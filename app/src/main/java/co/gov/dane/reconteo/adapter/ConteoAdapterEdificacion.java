package co.gov.dane.reconteo.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import co.gov.dane.reconteo.model.ConteoEdificacion;
import co.gov.dane.reconteo.Formulario;
import co.gov.dane.reconteo.FormularioEdificacion;
import co.gov.dane.reconteo.R;

public class ConteoAdapterEdificacion extends RecyclerView.Adapter<ConteoAdapterEdificacion.MyViewHolder> implements
        ItemTouchHelperAdapter {

    private List<ConteoEdificacion> formulario;
    private Formulario form;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(formulario, fromPosition, toPosition);
        ConteoEdificacion targetUser = formulario.get(fromPosition);
        formulario.remove(fromPosition);
        formulario.add(toPosition, targetUser);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public TextView id, fecha, nombre;
        public ImageView eliminar_item, editar_item;
        public LinearLayout contenedor;
        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.identificador);
            nombre = (TextView) view.findViewById(R.id.nombre);
            fecha = (TextView) view.findViewById(R.id.fecha_conteo);
            eliminar_item = view.findViewById(R.id.eliminar_item);
            contenedor = (LinearLayout) view.findViewById(R.id.contenedor);

        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }

    public ConteoAdapterEdificacion(List<ConteoEdificacion> moviesList, Formulario form) {
        this.formulario = moviesList;
        this.form=form;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.formulario_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ConteoEdificacion edificacion = formulario.get(position);
        holder.id.setText(edificacion.getId_edificacion());
        holder.nombre.setText(edificacion.getNombre());
        holder.fecha.setText(edificacion.getFecha());
        holder.eliminar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form.showDialogBorrarConteo(position);
            }
        });
        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = position;
                String id_manzana = formulario.get(itemPosition).getId_manzana();
                String id_edificacion = formulario.get(itemPosition).getId_edificacion();
                Intent formulario = new Intent(v.getContext(), FormularioEdificacion.class);
                formulario.putExtra("id_manzana", id_manzana);
                formulario.putExtra("id_edificacion", id_edificacion);
                v.getContext().startActivity(formulario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return formulario.size();
    }


}