package com.example.fresh_mart

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val shape = findViewById<ImageView>(R.id.shapeImage)

        // Start NORMAL (no shrink)
        shape.scaleX = 1f
        shape.scaleY = 1f
        shape.alpha = 1f

        shape.post {

            // Smooth scale animation
            val scaleX = ObjectAnimator.ofFloat(shape, "scaleX", 1f, 4f)
            val scaleY = ObjectAnimator.ofFloat(shape, "scaleY", 1f, 4f)

            // Optional slight fade for premium feel
            val fade = ObjectAnimator.ofFloat(shape, "alpha", 1f, 1f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY, fade)

            animatorSet.duration = 1800
            animatorSet.interpolator = AccelerateDecelerateInterpolator()

            animatorSet.start()

            // Move to next screen after animation
            shape.postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 1800)
        }
    }
}