package com.devpaul.estructurapublicitarias_roal.domain.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.devpaul.estructurapublicitarias_roal.R
import com.devpaul.estructurapublicitarias_roal.data.models.entity.Options
import com.devpaul.estructurapublicitarias_roal.domain.utils.startNewActivityWithAnimation
import com.devpaul.estructurapublicitarias_roal.view.management_worker.managementWorker.ManagementWorkerActivity
import com.devpaul.estructurapublicitarias_roal.view.validationepp.ValidationEPPActivity

class OptionsAdapter(val context: Activity, private val categories: List<Options>) :
    RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_options, parent, false)
        return OptionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {

        val category = categories[position] // CADA UNA DE LAS CATEGORIAS

        holder.textViewCategory.text = category.name

        Glide.with(context)
            .load(category.image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.imageViewCategory)

        holder.itemView.setOnClickListener { goToProducts(category) }

    }


    private fun goToProducts(options: Options) {

        when (options.optionID) {
            "1" -> {
                startNewActivityWithAnimation(context, ManagementWorkerActivity::class.java)
            }

            "2" -> {
                startNewActivityWithAnimation(context, ValidationEPPActivity::class.java)
            }

            "3" -> {
                //            val intent = Intent(context, ::class.java)
                //            val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_in, R.transition.slide_out)
                //            context.startActivity(intent, options.toBundle())
            }

            "4" -> {
                //            val intent = Intent(context, ValidationEPPActivity::class.java)
                //            val options = ActivityOptionsCompat.makeCustomAnimation(context, R.transition.slide_in, R.transition.slide_out)
                //            context.startActivity(intent, options.toBundle())
            }
        }

    }

    class OptionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }
    }

}