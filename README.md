# Kakyll


## Install

TODO (brew)

## Getting Started

First we tell Kakyll to create a new site.
```bash
kakyll new my_site
```

Next, navigate into the newly created site.
```bash
cd my_site
```
This will have pre-generate a sample blog and a pre-configured default blog structure. The blog structure and configuration are explained below.


kakyll clean
kakyll build
kakyll serve (also performs build)
kakyll deploy


## Blog Structure

```bash
.
├── templates
|   └── default.hbs
|   ├── footer.hbs
|   └── header.hbs
|   └── post.hbs
├── posts
|   ├── 2017-05-17-birth-of-kakyll.md
|   └── 2017-05-18-kotlin-is-awesome.md
├──-assets
|   ├── css
|   |   ├── style.css
|   |   └── style.scss
|   ├── js
|   |   └── scripts.js
|   ├── images
|   |   └── my_logo.png
├── index.hbs
├── about.hbs
└── config.yml
```

Upon building your site, all contents will be copied to `_site`

## Configuration

TODO explanation/table


Sample default config.yml file
```yaml
title: My Blog Title
email: you@email.com
description: My Blog is about random stuff like lorem ipsum and programming. I hope you enjoy.
base_url: http://localhost:8080 # The base url to your blog. e.g. http://hakyll.com
template_engine: handlebars
deploy: rsync -avz ....         # command to execut to deploy contents in _site/*
encoding: UTF-8                 # encoding of files, defaults to UTF-8
posts:                          # configuration for your blog's posts
    directory: posts            # directory where your posts are located
    template: post.hbs          # optional template for posts
pages:
    - index.html
    - about.html
directories:
    - assets
```

## Template Engine

Currently the only supported template engine is `Handlebars`, but there will be more to come.

Planned to support:
- Groovy Simple Template Engine
- Rythm
- Thymeleaf
- Jtwig
- Liquid

## Markdown Engine

Kakyll used [FlexMark](https://github.com/vsch/flexmark-java) as it's primary markdown renderer.
Other options are still being considered.
This will also become configurable.


## Styling Your Blog

By default the blog is themed using Bootstrap based of some examples from [this](http://prideparrot.com/blog/archive/2014/4/blog_template_using_twitter_bootstrap3_part1#creating-a-blog-template) blog post.
The author, Vijaya Anand, put together an immense and detailed post explaining how to use Bootstrap to create a nice and clean blog template.

The current default template for a post.

<img src="https://github.com/kennycason/kakyll/blob/master/screenshots/post_sample.png?raw=true"/>
