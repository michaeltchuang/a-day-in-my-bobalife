const appVersionName = "2023.03.11"

module.exports = {
  branches: ["main"],
  tagFormat: "v${appVersionName}",
  plugins: [
    [
      "@semantic-release/commit-analyzer",
      {
        preset: "angular",
        releaseRules: [
            {type: 'feat', release: 'minor'},
            {type: 'ci', release: 'patch'},
            {type: 'fix', release: 'patch'},
            {type: 'docs', release: 'patch'},
            {type: 'test', release: 'patch'},
            {type: 'refactor', release: 'patch'},
            {type: 'style', release: 'patch'},
            {type: 'build', release: 'patch'},
            {type: 'chore', release: 'patch'},
            {type: 'revert', release: 'patch'}
       ]
      }
    ],
    [
      "@semantic-release/release-notes-generator",
      {
        preset: "conventionalcommits",
        presetConfig: {
          types: [
            {
              "type": "feat",
              "section": "Features",
              "hidden": false
            },
            {
              "type": "fix",
              "section": "Bug Fixes",
              "hidden": false
            },
            {
              "type": "docs",
              "section": "Documentation",
              "hidden": false
            },
            {
              "type": "style",
              "section": "Updates & Maintenance",
              "hidden": false
            },
            {
              "type": "refactor",
              "section": "Updates & Maintenance",
              "hidden": false
            },
            {
              "type": "perf",
              "section": "Updates & Maintenance",
              "hidden": false
            },
            {
              "type": "test",
              "section": "Updates & Maintenance",
              "hidden": false
            },
            {
              "type": "ci",
              "section": "Updates & Maintenance",
              "hidden": false
            },
            {
              "type": "chore",
              "section": "Updates & Maintenance",
              "hidden": false
            }
          ]
        }
      }
    ],
    [
      "@semantic-release/changelog",
      {
        changelogFile: "CHANGELOG.md",
      },
    ],
    [
      "@semantic-release/exec",
      {
        prepareCmd: "sh ./scripts/release.sh ${appVersionName}",
      },
    ],
    ["@semantic-release/github"],
    [
      "@semantic-release/git",
      {
        assets: ["CHANGELOG.md", "build.gradle", "README.md"],
        message:
          "chore: ${appVersionName} (release) \n\n${nextRelease.notes}",
      },
    ],
  ],
};