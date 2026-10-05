package com.brandon3055.draconicevolution.client.render.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class DEItemTransforms {

    public static final ItemTransforms DEFAULT_BLOCK;
    public static final ItemTransforms DEFAULT_ITEM;
    public static final ItemTransforms DEFAULT_TOOL;
    public static final ItemTransforms DEFAULT_BOW;

    static {
        Map<ItemDisplayContext, ItemTransform> map;
        ItemTransform thirdPerson;
        ItemTransform firstPerson;

        //@formatter:off
        map = new HashMap<>();
        thirdPerson =                                   create(0F,2.5F, 0F,75F, 45F, 0F,0.375F );
        map.put(ItemDisplayContext.GUI,                      create(0F,  0F, 0F,30F,225F, 0F,0.625F));
        map.put(ItemDisplayContext.GROUND,                   create(0F,  3F, 0F, 0F,  0F, 0F, 0.25F));
        map.put(ItemDisplayContext.FIXED,                    create(0F,  0F, 0F, 0F,  0F, 0F,  0.5F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,  thirdPerson);
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND,   flipLeft(thirdPerson));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,  create(0F, 0F, 0F, 0F, 45F, 0F, 0.4F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND,   create(0F, 0F, 0F, 0F, 225F, 0F, 0.4F));
        DEFAULT_BLOCK = of(map);

        map = new HashMap<>();
        thirdPerson =                                    create(   0F,  3F,   1F, 0F,  0F, 0F, 0.55F);
        firstPerson =                                    create(1.13F,3.2F,1.13F, 0F,-90F,25F, 0.68F);
        map.put(ItemDisplayContext.GROUND,                    create(   0F,  2F,   0F, 0F,  0F, 0F, 0.5F));
        map.put(ItemDisplayContext.HEAD,                      create(   0F, 13F,   7F, 0F,180F, 0F,   1F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,   thirdPerson);
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND,    flipLeft(thirdPerson));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,   firstPerson);
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND,    flipLeft(firstPerson));
        DEFAULT_ITEM = of(map);

        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND,                   create(   0F,  2F,   0F, 0F,  0F, 0F, 0.5F));
        map.put(ItemDisplayContext.FIXED,                    create(   0F,  0F,   0F, 0F,180F, 0F,   1F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,  create(   0F,  4F, 0.5F, 0F,-90F, 55,0.85F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND,   create(   0F,  4F, 0.5F, 0F, 90F,-55,0.85F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,  create(1.13F,3.2F,1.13F, 0F,-90F, 25,0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND,   create(1.13F,3.2F,1.13F, 0F, 90F,-25,0.68F));
        DEFAULT_TOOL = of(map);

        map = new HashMap<>();
        map.put(ItemDisplayContext.GROUND,                   create(   0F,  2F,   0F,  0F,   0F,  0F, 0.5F));
        map.put(ItemDisplayContext.FIXED,                    create(   0F,  0F,   0F, 0F,  180F,  0F,   1F));
        map.put(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,  create(  -1F, -2F, 2.5F,-80F, 260F,-40F, 0.9F));
        map.put(ItemDisplayContext.THIRD_PERSON_LEFT_HAND,   create(  -1F, -2F, 2.5F,-80F,-280F, 40F, 0.9F));
        map.put(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,  create(1.13F,3.2F,1.13F,  0F, -90F, 25F,0.68F));
        map.put(ItemDisplayContext.FIRST_PERSON_LEFT_HAND,   create(1.13F,3.2F,1.13F,  0F,  90F,-25F,0.68F));
        DEFAULT_BOW = of(map);
        //@formatter:on
    }

    public static ItemTransform create(float tx, float ty, float tz, float rx, float ry, float rz, float s) {
        return new ItemTransform(new Vector3f(rx, ry, rz), new Vector3f(tx / 16, ty / 16, tz / 16), new Vector3f(s, s, s));
    }

    public static ItemTransform flipLeft(ItemTransform transform) {
        return new ItemTransform(
                new Vector3f(transform.rotation().x(), -transform.rotation().y(), -transform.rotation().z()),
                new Vector3f(-transform.translation().x(), transform.translation().y(), transform.translation().z()),
                transform.scale()
        );
    }

    /**
     * Left hand transforms are given as they apply; vanilla mirrors left hand transforms again when applying them.
     */
    public static ItemTransforms of(Map<ItemDisplayContext, ItemTransform> map) {
        return new ItemTransforms(
                left(map.get(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)),
                get(map, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND),
                left(map.get(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)),
                get(map, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND),
                get(map, ItemDisplayContext.HEAD),
                get(map, ItemDisplayContext.GUI),
                get(map, ItemDisplayContext.GROUND),
                get(map, ItemDisplayContext.FIXED),
                get(map, ItemDisplayContext.ON_SHELF),
                ImmutableMap.of()
        );
    }

    private static ItemTransform get(Map<ItemDisplayContext, ItemTransform> map, ItemDisplayContext context) {
        return map.getOrDefault(context, ItemTransform.NO_TRANSFORM);
    }

    private static ItemTransform left(ItemTransform transform) {
        return transform == null ? ItemTransform.NO_TRANSFORM : flipLeft(transform);
    }
}
