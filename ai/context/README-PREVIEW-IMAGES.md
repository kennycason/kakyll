# Preview Images in Kakyll

This document explains how to use the preview image feature in Kakyll.

## Overview

Kakyll now supports adding preview images to blog posts. These images can be used for:

1. Social media sharing (Open Graph and Twitter cards)
2. Displaying images in post listings
3. Displaying images in the post itself

## Adding a Preview Image to a Post

To add a preview image to a post, include the `preview_image` field in the front matter of your markdown file:

```markdown
---
title: My Blog Post
author: John Doe
tags: example, blog
preview_image: /assets/images/my-preview-image.jpg
description: A brief description of my blog post
---

Post content here...
```

The `preview_image` should be a path relative to your site's root. For example, if your image is in the `assets/images` directory, the path would be `/assets/images/my-preview-image.jpg`.

## How It Works

When Kakyll processes your markdown files:

1. It extracts the `preview_image` field from the front matter
2. It makes this field available to your templates as `{{ preview_image }}`
3. The default templates are set up to use this field to:
   - Add Open Graph and Twitter card meta tags to the HTML head
   - Display the image in the post itself
   - Display a thumbnail in post listings

## Default Template Features

### Meta Tags for Social Media

The default template includes Open Graph and Twitter card meta tags that use your preview image:

```html
{{#if preview_image}}
<!-- Open Graph / Facebook -->
<meta property="og:type" content="article">
<meta property="og:title" content="{{ title }}">
<meta property="og:description" content="{{ description }}">
<meta property="og:image" content="{{ absolute_url }}{{ preview_image }}">
<meta property="og:url" content="{{ absolute_url }}">

<!-- Twitter -->
<meta name="twitter:card" content="summary_large_image">
<meta name="twitter:title" content="{{ title }}">
<meta name="twitter:description" content="{{ description }}">
<meta name="twitter:image" content="{{ absolute_url }}{{ preview_image }}">
{{/if}}
```

### Displaying the Image in Posts

The post template displays the preview image at the top of the post:

```html
{{#if preview_image}}
<div class="post-preview-image">
    <img src="{{ preview_image }}" alt="{{ title }}" class="img-responsive">
</div>
{{/if}}
```

### Displaying Thumbnails in Post Listings

The default template displays thumbnails in the latest posts section:

```html
{{#each posts}}
    <li class="list-group-item">
        <div class="row">
            {{#if this.preview_image}}
            <div class="col-md-3">
                <img src="{{ this.preview_image }}" alt="{{ this.title }}" class="img-responsive thumbnail">
            </div>
            <div class="col-md-9">
            {{else}}
            <div class="col-md-12">
            {{/if}}
                <a href="{{ this.url }}">{{ this.title }} - {{ this.date }}</a>
            </div>
        </div>
    </li>
{{/each}}
```

## Best Practices

1. Use appropriately sized images for preview images (recommended: 1200x630 pixels for optimal social media sharing)
2. Include a descriptive `alt` text in your templates for accessibility
3. Always include a `description` field in your front matter for better social media sharing
4. Store your images in the `assets/images` directory for better organization
