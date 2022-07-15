package co.gov.dane.recuento.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.gov.dane.recuento.Formulario;
import co.gov.dane.recuento.MainActivity;
import co.gov.dane.recuento.R;
import co.gov.dane.recuento.backend.Database;
import co.gov.dane.recuento.model.ConteoManzana;

public class    ConteoAdapterManzana extends RecyclerView.Adapter<ConteoAdapterManzana.MyViewHolder> implements
        ItemTouchHelperAdapter {

    private List<ConteoManzana> formulario;
    private List<ConteoManzana> temp;

    private Boolean click_long = false;
    private MainActivity main;

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public TextView id, fecha, nombre, tv_resumenManzana;
        public LinearLayout linear_manzana, linear_item;
        public ImageView imageView, sincronizado, doble_sincronizado, sincronizado_nube;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.identificador);
            nombre = (TextView) view.findViewById(R.id.nombre);
            fecha = (TextView) view.findViewById(R.id.fecha_conteo);
            tv_resumenManzana = (TextView) view.findViewById(R.id.tv_resumenManzana);
            linear_manzana = (LinearLayout) view.findViewById(R.id.linear_manzana);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            sincronizado = (ImageView) view.findViewById(R.id.sincronizado);
            doble_sincronizado = (ImageView) view.findViewById(R.id.doble_sincronizado);
            sincronizado_nube = (ImageView) view.findViewById(R.id.sincronizado_nube);
            linear_item = (LinearLayout) view.findViewById(R.id.linear_manzana);
            // itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }

    public Filter getFilter() {
        return myFilter;
    }

    public void getList(List<ConteoManzana> temp) {
        this.temp = temp;
    }

    Filter myFilter = new Filter() {
        // Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ConteoManzana> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.equals("") || charSequence.length() == 0) {
                filteredList.addAll(temp);
            } else {
                for (ConteoManzana event : formulario) {
                    if (event.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(event);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        // Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            formulario.clear();
            formulario.addAll((List<ConteoManzana>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public ConteoAdapterManzana(List<ConteoManzana> moviesList,MainActivity main) {
        this.formulario = moviesList;
        this.main=main;
    }

    public List<ConteoManzana> getFormulario() {
        return formulario;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.formulario_listado, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        ConteoManzana movie = formulario.get(position);

        holder.id.setText(movie.getId());
        holder.nombre.setText(movie.getNombre());
        holder.fecha.setText(movie.getFecha());

        if (movie.getFinalizado().equals("Si")) {
            Log.d("final:", "fin");
            holder.imageView.setColorFilter(ContextCompat.getColor(main, R.color.verde));
            holder.fecha.append("\nFormulario finalizado.");
            holder.nombre.setTextColor(ContextCompat.getColor(main, R.color.verde));
        } else {
            holder.imageView.setColorFilter(ContextCompat.getColor(main, R.color.negro));
            holder.nombre.setTextColor(ContextCompat.getColor(main, R.color.negro));
        }
        if (movie.getSincronizado()!= null && movie.getSincronizado().equals("Si")) {
            holder.sincronizado
                    .setColorFilter(ContextCompat.getColor(main, R.color.poligono_touch_boundary));
        }
        if (movie.getDoble_sincronizado()!= null && movie.getDoble_sincronizado().equals("Si")) {
            holder.doble_sincronizado
                    .setColorFilter(ContextCompat.getColor(main, R.color.poligono_touch_boundary));
        }

        if (movie.getSincronizado_nube()!= null && movie.getSincronizado_nube().equals("Si")) {
            holder.sincronizado_nube
                    .setColorFilter(holder.linear_item.getResources().getColor(R.color.poligono_touch_boundary));
        }

        if (main.select_all) {
            click_long = true;
            if (movie.getFinalizado().equals("Si")) {
                movie.setEnviar(1);
                holder.linear_item
                        .setBackgroundColor(holder.linear_item.getResources().getColor(R.color.poligono_touch));

            }
        } else {
            movie.setEnviar(0);
            holder.linear_item.setBackgroundColor(Color.TRANSPARENT);
        }
        if (main.cancel_sel == 1) {
            click_long = false;
            movie.setEnviar(0);
            holder.linear_item.setBackgroundColor(Color.TRANSPARENT);
            main.cancel_sel = 0;
        }
        holder.linear_manzana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click_long) {
                    int itemPosition = position;
                    String item = formulario.get(itemPosition).getId();
                    Intent formulario = new Intent(v.getContext(), Formulario.class);
                    formulario.putExtra("id_manzana", item);
                    v.getContext().startActivity(formulario);
                    ((Activity) v.getContext()).finish();
                } else {
                    if (movie.getFinalizado().equals("Si") && movie.getSincronizado_nube().equals("No")
                            && movie.getSincronizado().equals("No") && movie.getDoble_sincronizado().equals("No")) {
                        click_long = true;
                        Drawable background = holder.linear_item.getBackground();
                        int color = Color.TRANSPARENT;

                        if (background instanceof ColorDrawable) {
                            color = ((ColorDrawable) background).getColor();
                            Log.d("color:", String.valueOf(color));
                            if (color == Color.parseColor("#50A3E8FF")) {
                                movie.setEnviar(0);
                                holder.linear_item.setBackgroundColor(Color.TRANSPARENT);
                            } else {
                                movie.setEnviar(1);
                                holder.linear_item.setBackgroundColor(
                                        holder.linear_item.getResources().getColor(R.color.poligono_touch));
                            }
                        }
                    }
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = position;
                String id_manzana = formulario.get(itemPosition).getId();
                Database db = new Database(v.getContext());
                String res = db.getResumen(id_manzana);

                res = res.replace(",", "\n");
                LayoutInflater inflater = (LayoutInflater) v.getContext()
                        .getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
                final View mView = inflater.inflate(R.layout.dialog_resumen, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                LinearLayout dialog_close_resumen = (LinearLayout) mView.findViewById(R.id.dialog_close_resumen);

                TextView tv_resumenManzana = (TextView) mView.findViewById(R.id.tv_resumenManzana);;
                tv_resumenManzana.setText(id_manzana);

                TextView resumen = (TextView) mView.findViewById(R.id.resumen);
                resumen.setText(res);
                dialog_close_resumen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }
        });
        holder.linear_manzana.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (movie.getFinalizado().equals("Si") && movie.getSincronizado_nube().equals("No")
                        && movie.getSincronizado().equals("No") && movie.getDoble_sincronizado().equals("No")) {
                    main.verMensaje();
                    click_long = true;
                    Drawable background = holder.linear_item.getBackground();
                    int color = Color.TRANSPARENT;
                    if (background instanceof ColorDrawable) {
                        color = ((ColorDrawable) background).getColor();
                        if (color == holder.linear_item.getResources().getColor(R.color.poligono_touch)) {
                            movie.setEnviar(0);
                            holder.linear_item.setBackgroundColor(color);
                        } else {
                            movie.setEnviar(1);
                            holder.linear_item.setBackgroundColor(
                                    holder.linear_item.getResources().getColor(R.color.poligono_touch));
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return formulario.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(formulario, fromPosition, toPosition);
        ConteoManzana targetUser = formulario.get(fromPosition);
        formulario.remove(fromPosition);
        formulario.add(toPosition, targetUser);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
    }


}