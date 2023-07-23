package com.srikanth.mirrarapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode


class ArHome : AppCompatActivity() {
    private var arCam: ArFragment? = null
    private var btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arCam = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?
        btn=findViewById(R.id.btnColor)

        arCam?.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            placeObject(arCam!!, anchor)
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show()
        }

    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor) {
        val model = ModelRenderable.builder()
            .setSource(this, R.raw.lilly)
            .setIsFilamentGltf(true)
            .build()
        //when the model render is build add node to scene
        model.thenAccept { renderableObject ->
            addModel(
                fragment,
                anchor,
                renderableObject
            )
        }
        //handle error
        model.exceptionally {
            val toast = Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
            toast.show()
            null
        }
    }

    private fun addModel(fragment: ArFragment, anchor: Anchor, renderableObject: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val transformableNode = TransformableNode(fragment.transformationSystem)
        transformableNode.renderable = renderableObject
        transformableNode.setParent(anchorNode)
        fragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()

        btn?.setOnClickListener {
           try {
               renderableObject.material.setFloat2("basee", 255.0f, 0.0f)
           }catch (e:java.lang.Exception){}
        }

    }

}