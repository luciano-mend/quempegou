package br.luciano.quempegou.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import br.luciano.quempegou.R;
import br.luciano.quempegou.models.Amigo;

public class AmigoAdapter extends BaseAdapter {

    private Context context;
    private List<Amigo> listaAmigos;

    private static class AmigoHolder {
        public TextView txvValorNome;
        public TextView txvValorInclusao;
        public TextView txvValorAtivo;
    }

    public AmigoAdapter(Context context, List<Amigo> listaAmigos) {
        this.context = context;
        this.listaAmigos = listaAmigos;
    }

    @Override
    public int getCount() {
        return listaAmigos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaAmigos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        AmigoHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_amigos, viewGroup, false);

            holder = new AmigoHolder();
            holder.txvValorNome = view.findViewById(R.id.txvValorNome);
            holder.txvValorInclusao = view.findViewById(R.id.txvValorInclusao);
            holder.txvValorAtivo = view.findViewById(R.id.txvValorAtivo);

            view.setTag(holder);
        } else {
            holder = (AmigoHolder) view.getTag();
        }

        Amigo amigo = listaAmigos.get(i);

        holder.txvValorNome.setText(amigo.getNome());

        java.text.DateFormat format = DateFormat.getDateFormat(context);
        holder.txvValorInclusao.setText(format.format(new Date(amigo.getDataInclusao())));

        holder.txvValorAtivo.setText(amigo.isAtivo() ? R.string.sim_ativo : R.string.nao_ativo);

        return view;
    }
}
