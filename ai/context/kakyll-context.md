# Kakyll Project Context

## Overview
Kakyll is a static site generator written in Kotlin, similar to Jekyll (Ruby) or Hakyll (Haskell). It allows users to create static websites and blogs with Markdown content and Handlebars templates.

## Key Features
- Static site generation from Markdown content
- Handlebars templating
- YAML front matter for page metadata
- Tag-based categorization of posts
- Preview images for social media sharing
- Responsive design using Bootstrap
- Local development server
- Deployment via rsync

## Project Structure

### Core Components
- **Kakyll.kt**: Main entry point that parses commands and delegates to the appropriate handler
- **Structures.kt**: Constants defining the default directory structure and file names
- **cmd/**: Command handlers for various operations (new, build, serve, etc.)
- **config/**: Configuration loading and data classes
- **view/**: Rendering logic for pages, posts, and templates
- **view/render/**: Page renderers for different file types (Markdown, HTML, etc.)
- **view/template/**: Template engine implementations (currently only Handlebars)

### Default Site Structure
```
site/
├── _site/                # Generated site (output)
├── assets/               # Static assets
│   ├── css/              # CSS stylesheets
│   ├── js/               # JavaScript files
│   └── images/           # Images
├── posts/                # Markdown blog posts
├── templates/            # Handlebars templates
│   ├── default.hbs       # Main layout template
│   └── post.hbs          # Blog post template
├── about.hbs             # About page
├── config.yml            # Site configuration
├── index.hbs             # Homepage
└── tags.hbs              # Tags page
```

### Command Line Interface
- `kakyll new <site_name>`: Create a new site
- `kakyll new post <post_name>`: Create a new blog post
- `kakyll build`: Build the site
- `kakyll serve`: Start a local development server
- `kakyll clean`: Clean the output directory
- `kakyll deploy`: Deploy the site using the command in config.yml
- `kakyll version`: Display the version number

## Key Implementation Details

### Site Generation Process
1. **Configuration Loading**: Load site configuration from config.yml
2. **Post Processing**: Parse Markdown files with YAML front matter
3. **Template Application**: Apply Handlebars templates to content
4. **Tag Processing**: Generate tag pages and tag cloud
5. **Asset Copying**: Copy static assets to the output directory
6. **HTML Generation**: Write final HTML files to the output directory

### Preview Images Feature
- Posts can include a `preview_image` field in the YAML front matter
- The image is displayed in the post template
- Meta tags for social media sharing (Open Graph, Twitter) are generated
- Thumbnails are displayed in post listings

## Current Limitations and Improvement Areas
1. Limited template engine support (only Handlebars)
2. Basic styling with minimal custom CSS
3. No built-in support for Sass/SCSS
4. No pagination for post listings
5. No search functionality
6. No draft posts or scheduling
7. Limited documentation
8. No theme system
9. No plugin architecture
10. No internationalization support

## Dependencies
- Kotlin Standard Library
- Handlebars for templating
- FlexMark for Markdown processing
- Bootstrap for styling
- Jetty for the development server
- Jackson for YAML parsing
- Commons IO for file operations

## Testing
- Limited test coverage
- FlexMarkPageRendererTest tests the Markdown rendering and front matter extraction

## Build and Distribution
- Maven build system
- Distributed as a JAR file
- Homebrew formula for macOS installation
