/*
 *  All works are implicitly copyright jasperbouwman, but were provided for
 *  free/open community use without license restrictions
 */
package playerheads.library.jasperbouwman.util;

import org.bukkit.Axis;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;

/**
 * Utility class for setting block rotations for newer versions of minecraft with BlockData
 * see https://bukkit.org/threads/set-block-direction-easy.474786/ .
 * @author jasperbouwman (The_Spaceman)
 */
public class BlockRotationUtil {
   
    /**
     * see https://bukkit.org/threads/set-block-direction-easy.474786/
     * @author The_Spaceman
     */
    public static Axis convertBlockFaceToAxis(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return Axis.Z;
            case EAST:
            case WEST:
                return Axis.X;
            case UP:
            case DOWN:
                return Axis.Y;
                default:
                    return Axis.X;
        }
    }
    /**
     * see https://bukkit.org/threads/set-block-direction-easy.474786/ .
     * @author The_Spaceman
     */
    public static void setBlockRotation(Block block, BlockFace blockFace) {
        BlockState state = block.getState();
        BlockData blockData = state.getBlockData();
        if (blockData instanceof Directional) {
            System.out.println("Directional : "+blockFace);//TODO: DEBUG
            ((Directional) blockData).setFacing(blockFace);
        }
        if (blockData instanceof Orientable) {
            System.out.println("Orientable : "+blockFace);//TODO: DEBUG
            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(blockFace));
        }
        if (blockData instanceof Rotatable) {
            System.out.println("Rotable : "+blockFace);//TODO: DEBUG
            ((Rotatable) blockData).setRotation(blockFace);
        }
        state.setBlockData(blockData);
        state.update(false,true);
    }
}
