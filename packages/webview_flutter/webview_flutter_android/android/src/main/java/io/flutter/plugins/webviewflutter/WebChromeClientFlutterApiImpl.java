// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.webviewflutter;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugins.webviewflutter.GeneratedAndroidWebView.WebChromeClientFlutterApi;
import android.webkit.ValueCallback;
import android.net.Uri;
import io.flutter.plugins.webviewflutter.GeneratedAndroidWebView.WebChromeClientFlutterApi;
import java.io.File;
import java.util.List;

/**
 * Flutter Api implementation for {@link WebChromeClient}.
 *
 * <p>
 * Passes arguments of callbacks methods from a {@link WebChromeClient} to Dart.
 */
public class WebChromeClientFlutterApiImpl extends WebChromeClientFlutterApi {
  private final InstanceManager instanceManager;

  /**
   * Creates a Flutter api that sends messages to Dart.
   *
   * @param binaryMessenger handles sending messages to Dart
   * @param instanceManager maintains instances stored to communicate with Dart
   *                        objects
   */
  public WebChromeClientFlutterApiImpl(
      BinaryMessenger binaryMessenger, InstanceManager instanceManager) {
    super(binaryMessenger);
    this.instanceManager = instanceManager;
  }

  @Override
  public boolean onShowFileChooser(
      WebView view,
      ValueCallback<Uri[]> filePathCallback,
      WebChromeClient.FileChooserParams fileChooserParams) {
    if (flutterApi != null) {
      flutterApi.onShowFileChooser(
          this,
          view,
          new WebChromeClientFlutterApi.Reply<List<String>>() {
            public void reply(List<String> paths) {
              final Uri[] uris = new Uri[paths.size()];
              for (int i = 0; i < uris.length; i++) {
                uris[i] = Uri.fromFile(new File(paths.get(i)));
              }
              filePathCallback.onReceiveValue(uris);
            }
          });
      return true;
    }
    filePathCallback.onReceiveValue(null);
    return true;
  }

  /** Passes arguments from {@link WebChromeClient#onProgressChanged} to Dart. */
  public void onProgressChanged(
      WebChromeClient webChromeClient, WebView webView, Long progress, Reply<Void> callback) {
    final Long webViewIdentifier = instanceManager.getIdentifierForStrongReference(webView);
    if (webViewIdentifier == null) {
      throw new IllegalStateException("Could not find identifier for WebView.");
    }
    super.onProgressChanged(
        getIdentifierForClient(webChromeClient), webViewIdentifier, progress, callback);
  }

  /** Passes arguments from {@link WebChromeClient#onProgressChanged} to Dart. */
  public void onShowFileChooser(
      WebChromeClient webChromeClient, WebView webView, Reply<List<String>> callback) {
    super.onShowFileChooser(
        instanceManager.getInstanceId(webChromeClient),
        instanceManager.getInstanceId(webView),
        callback);
  }

  /**
   * Communicates to Dart that the reference to a {@link WebChromeClient}} was
   * removed.
   *
   * @param webChromeClient the instance whose reference will be removed
   * @param callback        reply callback with return value from Dart
   */
  public void dispose(WebChromeClient webChromeClient, Reply<Void> callback) {
    if (instanceManager.containsInstance(webChromeClient)) {
      dispose(getIdentifierForClient(webChromeClient), callback);
    } else {
      callback.reply(null);
    }
  }

  private long getIdentifierForClient(WebChromeClient webChromeClient) {
    final Long identifier = instanceManager.getIdentifierForStrongReference(webChromeClient);
    if (identifier == null) {
      throw new IllegalStateException("Could not find identifier for WebChromeClient.");
    }
    return identifier;
  }
}
