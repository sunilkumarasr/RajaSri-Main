package com.rss.rajasri.ui.plots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rss.rajasri.databinding.LayoutPlotNumberItemBinding
import com.rss.rajasri.datamodels.response.Plot

class AvailablePlotsAdapter(val activity:AvailablePlotListScreen):
    RecyclerView.Adapter<AvailablePlotsAdapter.AvailablePlotsViewHolder>() {

        lateinit var list:ArrayList<Plot>
        init {
            list= ArrayList();
        }
    class AvailablePlotsViewHolder(val binding: LayoutPlotNumberItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    fun setPlotList(lists:ArrayList<Plot>)
    {
        list.clear()
        list.addAll(lists)
    notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailablePlotsViewHolder {
        val binding=LayoutPlotNumberItemBinding.inflate(LayoutInflater.from(parent.context))
        return AvailablePlotsViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: AvailablePlotsViewHolder, position: Int) {

        val plot=list.get(position)
        holder.binding.txtPlotNumber.setOnClickListener {

        }
        holder.binding.txtPlotNumber.setText("${plot.plotNum}")
        if(plot.status!="1")
            holder.binding.txtPlotNumber.isChecked=true
        else
            holder.binding.txtPlotNumber.isChecked=false

        holder.binding.txtPlotNumber.setOnClickListener {
            if(plot.status!="1")
            {
                activity.showBookedDetails(plot)
            }else{
                activity.showOpenDetails(plot)
            }
        }
    }

}