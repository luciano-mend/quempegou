package br.luciano.quempegou.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.luciano.quempegou.R;
import br.luciano.quempegou.models.Emprestimo;

public class EmprestimoAdapter extends BaseAdapter {

    private Context context;
    private List<Emprestimo> listaEmprestimo;
    private String[] amigos;

    private static class EmprestimoHolder {
        public TextView txvValorItemEmprestado;
        public TextView txvValorNomeAmigo;
        public TextView txvValorPrioridadeDevolucao;
        public TextView txvValorItemFragil;
        public TextView tvxValorDevolucao;
        public TextView txvValorObservacoes;
    }

    public EmprestimoAdapter(Context context, List<Emprestimo> listaEmprestimo) {
        this.context = context;
        this.listaEmprestimo = listaEmprestimo;

        amigos = context.getResources().getStringArray(R.array.amigos);
    }

    @Override
    public int getCount() {
        return listaEmprestimo.size();
    }

    @Override
    public Object getItem(int i) {
        return listaEmprestimo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // padrão (pattern) holder
        EmprestimoHolder holder;

        // se a view é null ela não foi criada ainda, é a primeira execução
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_lista_emprestimos, viewGroup, false);

            holder = new EmprestimoHolder();
            holder.txvValorItemEmprestado = view.findViewById(R.id.txvValorItemEmprestado);
            holder.txvValorNomeAmigo = view.findViewById(R.id.txvValorNomeAmigo);
            holder.txvValorPrioridadeDevolucao = view.findViewById(R.id.txvValorPrioridadeDevolucao);
            holder.txvValorItemFragil = view.findViewById(R.id.txvValorItemFragil);
            holder.tvxValorDevolucao = view.findViewById(R.id.tvxValorDevolucao);
            holder.txvValorObservacoes = view.findViewById(R.id.txvValorObservacoes);

            view.setTag(holder);
        } else {
            holder = (EmprestimoHolder) view.getTag();
        }

        Emprestimo emprestimo = listaEmprestimo.get(i);

        holder.txvValorItemEmprestado.setText(emprestimo.getNomeItemEmprestado());

        holder.txvValorNomeAmigo.setText(amigos[emprestimo.getAmigo()]);

        switch (emprestimo.getPrioridadeDevolucao()){
            case VAZIO:
                holder.txvValorPrioridadeDevolucao.setText(R.string.prioridade_vazio);
                break;
            case BAIXA:
                holder.txvValorPrioridadeDevolucao.setText(R.string.prioridade_baixa);
                break;
            case ALTA:
                holder.txvValorPrioridadeDevolucao.setText(R.string.prioridade_alta);
                break;
        }

        holder.txvValorItemFragil.setText(emprestimo.isFragil() ? context.getString(R.string.item_fragil_sim) : context.getString(R.string.item_fragil_nao));
        holder.tvxValorDevolucao.setText(emprestimo.isDevolvido() ? context.getString(R.string.item_devolvido_sim) : context.getString(R.string.item_devolvido_nao));
        holder.txvValorObservacoes.setText(emprestimo.getObservacao());

        return view;
    }
}
