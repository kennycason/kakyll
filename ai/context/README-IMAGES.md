# Kakyll Image Support

Kakyll now supports enhanced image handling for your blog posts. This document explains how to use the new image features.

## Image Types

Kakyll supports three types of images:

1. **Main Images**: The primary image for a post, used in the post header and for social media sharing.
2. **Thumbnails**: Smaller versions of images used in post listings.
3. **Regular Images**: Additional images associated with a post.

## Adding Images to Posts

You can add images to your posts using the YAML front matter:

```yaml
---
title: My Post Title
author: Author Name
tags: tag1, tag2
main_image: /assets/images/main-image.jpg
thumbnail: /assets/images/thumbnail.jpg
images:
  - /assets/images/image1.jpg
  - /assets/images/image2.jpg
  - /assets/images/image3.jpg
---
```

### Main Image

The `main_image` field specifies the primary image for the post. This image will be:

- Displayed at the top of the post
- Used in Open Graph and Twitter meta tags for social media sharing
- Included in the "All Main Images" section of the Images page

### Thumbnail

The `thumbnail` field specifies a smaller image used in post listings. This image will be:

- Displayed in the "Latest Posts" sidebar
- Included in the "All Thumbnails" section of the Images page

### Regular Images

The `images` field is a list of additional images associated with the post. These images will be:

- Included in the "Post Images" section of the Images page
- Available for use in post carousels or galleries

## Images Page

Kakyll automatically generates an Images page that displays all images from all posts. The page is organized into sections:

1. **Main Images**: All main images from all posts
2. **Thumbnails**: All thumbnails from all posts
3. **Post Images**: All regular images from all posts
4. **All Images**: A combined gallery of all images

The Images page is accessible from the navigation menu and sidebar.

## Using Images in Templates

In your templates, you can access the image collections through the following variables:

- `all_images`: All images from all posts
- `all_main_images`: All main images from all posts
- `all_thumbnails`: All thumbnails from all posts

Each image object has the following properties:

- `path`: The path to the image file
- `title`: The title of the post containing the image
- `type`: The type of image ("main", "thumbnail", or "regular")
- `postUrl`: The URL of the post containing the image
- `postTitle`: The title of the post containing the image

Example usage in a template:

```handlebars
<div class="image-gallery">
  {{#each all_images}}
    <div class="image-card">
      <a href="{{this.postUrl}}">
        <img src="{{this.path}}" alt="{{this.title}}">
        <p>{{this.postTitle}}</p>
      </a>
    </div>
  {{/each}}
</div>
```

## Best Practices

1. **Use consistent image sizes**: Keep your main images, thumbnails, and regular images at consistent sizes for a more polished look.
2. **Optimize images**: Compress your images to reduce page load times.
3. **Use descriptive filenames**: Name your image files descriptively to make them easier to manage.
4. **Store images in the assets directory**: Keep all your images in the `assets/images` directory for better organization.
5. **Use relative paths**: Always use relative paths starting with a slash (e.g., `/assets/images/image.jpg`).
