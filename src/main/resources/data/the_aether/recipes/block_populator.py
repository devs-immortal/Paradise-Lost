import os

  
def pop_block_set(parent_id, fence_bool, wall_bool, cracked_bool):
    stair_file = open(parent_id + "_stairs.json", "x")
    slab_file = open(parent_id + "_slab.json", "x")
    if fence_bool:
        fence_file = open(parent_id + "_fence.json", "x")
        fence_gate_file = open(parent_id + "_fence_gate.json", "x")
        door_file = open(parent_id + "_door.json", "x")
        trapdoor_file = open(parent_id + "_trapdoor.json", "x")
    if wall_bool:
        wall_file = open(parent_id + "_wall.json", "x")
    if cracked_bool:
        cracked_file = open("cracked_" + parent_id + ".json", "x")
    
    if fence_bool:
        stair_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_stairs",
  "pattern": [
    "#  ",
    "## ",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_stairs",
    "count": 4
  }
}
        """)
        fence_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_fence",
  "pattern": [
    "W#W",
    "W#W"
  ],
  "key": {
    "W": {
      "item": "the_aether:"""+parent_id+"""_planks"
    },
    "#": {
      "item": "minecraft:stick"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_fence",
    "count": 3
  }
}
        """)
        fence_gate_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_fence_gate",
  "pattern": [
    "#W#",
    "#W#"
  ],
  "key": {
    "#": {
      "item": "minecraft:stick"
    },
    "W": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_fence_gate"
  }
}
        """)
        door_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_door",
  "pattern": [
    "##",
    "##",
    "##"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_door",
    "count": 3
  }
}
        """)
        trapdoor_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_trapdoor",
  "pattern": [
    "###",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_trapdoor",
    "count": 2
  }
}
        """)
    else:
        stair_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "#  ",
    "## ",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+""""
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_stairs",
    "count": 4
  }
}
        """)
    if fence_bool:
        slab_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "group": "wooden_slab",
  "pattern": [
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+"""_planks"
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_slab",
    "count": 6
  }
}
        """)
    else:
        slab_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+""""
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_slab",
    "count": 6
  }
}
        """)
    if wall_bool:
        wall_file.write(
        """
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "###",
    "###"
  ],
  "key": {
    "#": {
      "item": "the_aether:"""+parent_id+""""
    }
  },
  "result": {
    "item": "the_aether:"""+parent_id+"""_wall",
    "count": 6
  }
}
        """)
    if cracked_bool:
        cracked_file.write(
        """
{
  "type": "minecraft:smelting",
  "ingredient": {
    "item": "the_aether:"""+parent_id+""""
  },
  "result": "the_aether:cracked_"""+parent_id+"""",
  "experience": 0.1,
  "cookingtime": 200
}        
        """)
    
    stair_file.close()
    slab_file.close()
    if fence_bool:
        fence_file.close()
        fence_gate_file.close()
        door_file.close()
        trapdoor_file.close()
    if wall_bool:
        wall_file.close()
    if cracked_bool:
        cracked_file.close()
    
    
    
def pop_tool_set(material_name, mod_id):
    sword_file = open(material_name + "_sword.json", "x")
    pickaxe_file = open(material_name + "_pickaxe.json", "x")
    axe_file = open(material_name + "_axe.json", "x")
    shovel_file = open(material_name + "_shovel.json", "x")
    hoe_file = open(material_name + "_hoe.json", "x")
    
    sword_file.write(
    """

    """    
    )
    pickaxe_file.write(
    """

    """    
    )
    
    sword_file.close()
    pickaxe_file.close()
    axe_file.close()
    shovel_file.close()
    hoe_file.close()
    

while True:
    selection = input("\n\n What do you want to add?\n  0.) Quit\n  1.) Populate a block set\n ")
    if selection == "0":
        break
    elif selection == "1":
        id = input("  What is the parent block's name?\n ").lower()
        fence_bool = (input("  Are there fences, fencegates, and doors/trapdoors (wood set)?\n ").lower() == "y")
        wall_bool = (input("  Are there walls?\n ").lower() == "y")
        cracked_bool = (input("  Are there cracked variants?\n ").lower() == "y")
        pop_block_set(id, fence_bool, wall_bool, cracked_bool)
        os.system('cls')
    else:
        print(" Enter valid selection!")