const resourcesFolder = './main/resources/assets/paradise_lost/models/block';
const lootTableFolder = './main/resources/data/paradise_lost/loot_tables/blocks'
const fs = require('fs');

models = [];
alreadyDoneLoottables = [];

async function run() {
    fs.readdir(resourcesFolder, (err, files) => {
      if (err) throw err;
      files.forEach(file => {
        models.push(file.replace(".json", ""))
      });
      fs.readdir(lootTableFolder, (err, files) => {
              if (err) throw err;
              files.forEach(file => {
                  alreadyDoneLoottables.push(file.replace(".json", ""));
              });
              const intersection = models.filter(element => !alreadyDoneLoottables.includes(element));
              let data = "# Loot Tables TODO:\n\n";
              intersection.forEach(insect => {
                data += `- [ ] ${insect}\n`
              });
              fs.writeFileSync("./todo.md", data);
          });
    });
}

run();