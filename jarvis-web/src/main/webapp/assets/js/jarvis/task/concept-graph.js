//初始化对象
var CollapsibleTree = function (elt, w, h, initY) {
    //属性
    var m = [20, 120, 20, 120],
        w = w,
        h = h,
        initY = initY,
        i = 0,
        root,
        root2,
        childrenDepth = 0,
        parentsDepth = 0,
        levelSize = 5,
        parentLevelIndex = 0,
        childLevelIndex = 0;
    //初始化树
    var tree = d3.layout.tree()
        //.size([h, w]);
        .size([w, h]);
    // var diagonal = d3.svg.diagonal()
    //     .projection(function(d) { return [d.y, d.x]; });

    var parentdiagonal = d3.svg.diagonal()
        .projection(function (d) {
            return [d.x, -d.y];
        });

    var childdiagonal = d3.svg.diagonal()
        .projection(function (d) {
            return [d.x, d.y];
        });

    var vis = d3.select(elt).append("svg")
        .attr("width", w)
        .attr("height", h)
        .append("g")
        // .attr("transform", "translate(" + m[3] + "," + m[0] + ")"); // left-right
        // .attr("transform", "translate(" + m[0] + "," + m[3] + ")"); // top-bottom
        .attr("transform", "translate(0," + (initY) + ")"); // bidirectional-tree


    var that = {
        init: function (url) {
            var that = this;
            d3.json(url, function (json) {
                root = json;

                // root.x0 = h / 2;
                // root.y0 = 0;
                root.x0 = w / 2;
                root.y0 = initY;

                // Initialize the display to show a few nodes.
                root.children.forEach(that.toggleAll);
                // that.toggle(root.children[1]);
                // that.toggle(root.children[1].children[2]);
                // that.toggle(root.children[9]);
                // that.toggle(root.children[9].children[0]);

                that.updateBoth(root);
                //that.updateParents(root);
                //that.updateChildren(root);

            });
        },
        updateBoth: function (source) {
            var duration = d3.event && d3.event.altKey ? 5000 : 150;

            // Compute the new tree layout.

            var nodes = tree.nodes(root).reverse();

            // Normalize for fixed-depth.
            //根据树的深度设置y轴的值，即进行分层，每层固定
            nodes.forEach(function (d) {
                var y = 0;
                var x = 0;
                var interval = (w - 140) / (levelSize-1);

                if (d.parentFlag) {
                    parentLevelIndex++;
                    if (root.parents && root.parents.length > levelSize) {
                        var level = parentLevelIndex % levelSize == 0 ? parentLevelIndex / levelSize : parseInt(parentLevelIndex / levelSize) + 1;
                        y = level * 120;
                        var levelIndex = 1;
                        if (parentLevelIndex % levelSize == 0) {
                            levelIndex = levelSize;
                        }
                        else {
                            levelIndex = parentLevelIndex % levelSize;
                        }
                        var x = (levelIndex-1) * interval + 70;
                        d.x = x;
                        //console.log(d.jobName+","+levelIndex % (levelSize + 1) + "," + "x:" + x);
                    }
                    else {
                        y = d.depth * 120;
                    }

                }
                else if (d.rootFlag) {
                }
                else {
                    childLevelIndex++;
                    if (root.children && root.children.length > levelSize) {

                        var level = childLevelIndex % levelSize == 0 ? childLevelIndex / levelSize : parseInt(childLevelIndex / levelSize) + 1;
                        y = level * 120;
                        var levelIndex = 1;
                        if (childLevelIndex % levelSize == 0) {
                            levelIndex = levelSize;
                        }
                        else {
                            levelIndex = childLevelIndex % levelSize;
                        }

                        var x = (levelIndex-1) * interval + 70;
                        d.x = x;
                        //console.log(d.jobName+","+levelIndex % (levelSize + 1) + ",x:" + x);
                    }
                    else {
                        y = d.depth * 120;
                    }

                }
                if (d.y == undefined || d.y == null || d.y > y) {
                    d.y = y;
                }
            });

            // Update the nodes…
            var node = vis.selectAll("g.node")
                .data(nodes, function (d) {
                    return d.id || (d.id = ++i);
                });

            // Enter any new nodes at the parent's previous position.
            var nodeEnter = node.enter().append("svg:g")
                .attr("class", "node")
                .attr("transform", function (d) {
                    return "translate(" + source.x0 + "," + source.y0 + ")";
                });


            nodeEnter.append("svg:circle")
                .attr("r", 1e-6)
                .style("fill", function (d) {
                    return getColor(d)
                })
                .on("click", function (d) {
                    jumpToNode(d);
                }).on("mouseover", function (d) {
                    showTaskInfo(this, d);
                }).on("mouseout", function () {
                    hideTaskInfo(this);
                });
            ;

            nodeEnter.append("svg:text")
                // .attr("x", function(d) { return d.children || d._children ? -10 : 10; })
                .attr("x", "20")
                .attr("y", "30")
                .attr("dy", ".35em")
                // .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
                .attr("text-anchor", function (d) {
                    if (that.isParent(d)) {
                        return "end";
                    } else {
                        return d.children || d._children ? "end" : "start";
                    }
                })
                .attr("transform", function (d) {
                    if (d != root) {
                        if (that.isParent(d)) {
                            return "rotate(0)";
                        } else {
                            return "rotate(0)";
                        }
                    }
                })
                .text(function (d) {
                    return getNodeName(d);
                })
                .style("fill-opacity", 1e-6)
                .style("font-size", "16");
            ;

            // Transition nodes to their new position.
            var nodeUpdate = node.transition()
                .duration(duration)
                .attr("transform", function (d) {
                    if (that.isParent(d)) {
                        return "translate(" + d.x + "," + -d.y + ")";
                    } else {
                        return "translate(" + d.x + "," + d.y + ")";
                    }
                });
            //设置圆圈设置半径与颜色
            nodeUpdate.select("circle")
                .attr("r", 9)
                .style("fill", function (d) {
                    //return d._children ? "lightsteelblue" : "#4682B4";
                    if (d.rootFlag == true) {
                        return getColor(d);
                    }
                    else {
                        return getColor(d);
                    }
                });

            nodeUpdate.select("text")
                .style("fill-opacity", 1);

            // Transition exiting nodes to the parent's new position.
            var nodeExit = node.exit().transition()
                .duration(duration)
                // .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
                .attr("transform", function (d) {
                    return "translate(" + source.x + "," + source.y + ")";
                })
                .remove();

            nodeExit.select("circle")
                .attr("r", 1e-6);

            nodeExit.select("text")
                .style("fill-opacity", 1e-6);

            // Update the links…
            var link = vis.selectAll("path.link")
                .data(tree.links_parents(nodes).concat(tree.links(nodes)), function (d) {
                    return d.target.id;
                });
            //console.log(link);
            // Enter any new links at the parent's previous position.
            link.enter().insert("svg:path", "g")
                .attr("class", "link")
                .attr("d", function (d) {
                    var o = {x: source.x0, y: source.y0};
                    if (that.isParent(d.target)) {
                        return parentdiagonal({source: o, target: o});
                    } else {
                        // return parentdiagonal({source: o, target: o});
                        return childdiagonal({source: o, target: o});
                    }
                })
                .transition()
                .duration(duration)
                // .attr("d", parentdiagonal);
                .attr("d", function (d) {
                    if (that.isParent(d.target)) {
                        return parentdiagonal(d);
                    } else {
                        // return parentdiagonal(d);
                        return childdiagonal(d);
                    }
                })

            // Transition links to their new position.
            link.transition()
                .duration(duration)
                // .attr("d", parentdiagonal);
                .attr("d", function (d) {
                    if (that.isParent(d.target)) {
                        return parentdiagonal(d);
                    } else {
                        return childdiagonal(d);
                    }
                })

            // Transition exiting nodes to the parent's new position.
            link.exit().transition()
                .duration(duration)
                .attr("d", function (d) {
                    var o = {x: source.x, y: source.y};
                    // return parentdiagonal({source: o, target: o});
                    if (that.isParent(d.target)) {
                        return parentdiagonal({source: o, target: o});
                    } else {
                        return childdiagonal({source: o, target: o});
                    }
                })
                .remove();

            // Stash the old positions for transition.
            nodes.forEach(function (d) {
                d.x0 = d.x;
                d.y0 = d.y;
            });
        },
        updateParents: function (source) {
            var duration = d3.event && d3.event.altKey ? 5000 : 500;

            // Compute the new tree layout.
            var nodes = tree.nodes(root).reverse();

            // Normalize for fixed-depth.
            nodes.forEach(function (d) {
                d.y = d.depth * 180;
            });

            // Update the nodes…
            var node = vis.selectAll("g.node")
                .data(nodes, function (d) {
                    return d.id || (d.id = ++i);
                });

            // Enter any new nodes at the parent's previous position.
            var nodeEnter = node.enter().append("svg:g")
                .attr("class", "node")
                // .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
                .attr("transform", function (d) {
                    return "translate(" + source.x0 + "," + source.y0 + ")";
                })
                .on("click", function (d) {

                    that.toggle(d);
                    that.updateParents(d);
                });

            nodeEnter.append("svg:circle")
                .attr("r", 1e-6)
                .style("fill", function (d) {
                    return getColor(d)
                });

            nodeEnter.append("svg:text")
                .attr("x", "20")
                .attr("y", "30")
                .attr("dy", ".35em")
                .attr("text-anchor", function (d) {
                    return d.children || d._children ? "end" : "start";
                })
                .text(function (d) {
                    return getNodeName(d);
                })
                .style("fill-opacity", 1e-6)
                .style("font-size", "16");
            ;

            // Transition nodes to their new position.
            var nodeUpdate = node.transition()
                .duration(duration)
                .attr("transform", function (d) {
                    return "translate(" + d.x + "," + -d.y + ")";
                });

            nodeUpdate.select("circle")
                .attr("r", 4.5)
                .style("fill", function (d) {
                    return getColor(d)
                });

            nodeUpdate.select("text")
                .style("fill-opacity", 1);

            // Transition exiting nodes to the parent's new position.
            var nodeExit = node.exit().transition()
                .duration(duration)
                // .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
                .attr("transform", function (d) {
                    return "translate(" + source.x + "," + source.y + ")";
                })
                .remove();

            nodeExit.select("circle")
                .attr("r", 1e-6);

            nodeExit.select("text")
                .style("fill-opacity", 1e-6);

            // Update the links…
            var link = vis.selectAll("path.link")
                .data(tree.links(nodes), function (d) {
                    return d.target.id;
                });

            // Enter any new links at the parent's previous position.
            link.enter().insert("svg:path", "g")
                .attr("class", "link")
                .attr("d", function (d) {
                    var o = {x: source.x0, y: source.y0};
                    return parentdiagonal({source: o, target: o});
                })
                .transition()
                .duration(duration)
                .attr("d", parentdiagonal);

            // Transition links to their new position.
            link.transition()
                .duration(duration)
                .attr("d", parentdiagonal);

            // Transition exiting nodes to the parent's new position.
            link.exit().transition()
                .duration(duration)
                .attr("d", function (d) {
                    var o = {x: source.x, y: source.y};
                    return parentdiagonal({source: o, target: o});
                })
                .remove();

            // Stash the old positions for transition.
            nodes.forEach(function (d) {
                d.x0 = d.x;
                d.y0 = d.y;
            });
        },
        updateChildren: function (source) {
            var duration = d3.event && d3.event.altKey ? 5000 : 500;

            // Compute the new tree layout.
            var nodes = tree.nodes(root2).reverse();

            // Normalize for fixed-depth.
            nodes.forEach(function (d) {
                d.y = d.depth * 180;
            });

            // Update the nodes…
            var node = vis.selectAll("g.node")
                .data(nodes, function (d) {
                    return d.id || (d.id = ++i);
                });

            // Enter any new nodes at the parent's previous position.
            var nodeEnter = node.enter().append("svg:g")
                .attr("class", "node")
                // .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
                .attr("transform", function (d) {
                    return "translate(" + source.x0 + "," + source.y0 + ")";
                })
                .on("click", function (d) {
                    that.toggle(d);
                    that.updateChildren(d);
                });

            nodeEnter.append("svg:circle")
                .attr("r", 1e-6)
                .style("fill", function (d) {
                    return getColor(d)
                });

            nodeEnter.append("svg:text")
                .attr("x", "20")
                .attr("y", "30")
                .attr("dy", ".35em")
                .attr("text-anchor", function (d) {
                    return d.children || d._children ? "end" : "start";
                })
                .text(function (d) {
                    return getNodeName(d);
                })
                .style("fill-opacity", 1e-6)
                .style("font-size", "16");

            // Transition nodes to their new position.
            var nodeUpdate = node.transition()
                .duration(duration)
                // .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });
                .attr("transform", function (d) {
                    return "translate(" + d.x + "," + d.y + ")";
                });

            nodeUpdate.select("circle")
                .attr("r", 4.5)
                .style("fill", function (d) {
                    return getColor(d)
                });

            nodeUpdate.select("text")
                .style("fill-opacity", 1);

            // Transition exiting nodes to the parent's new position.
            var nodeExit = node.exit().transition()
                .duration(duration)
                // .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
                .attr("transform", function (d) {
                    return "translate(" + source.x + "," + source.y + ")";
                })
                .remove();

            nodeExit.select("circle")
                .attr("r", 1e-6);

            nodeExit.select("text")
                .style("fill-opacity", 1e-6);

            // Update the links…
            var link = vis.selectAll("path.link")
                .data(tree.links(nodes), function (d) {
                    return d.target.id;
                });

            // Enter any new links at the parent's previous position.
            link.enter().insert("svg:path", "g")
                .attr("class", "link")
                .attr("d", function (d) {
                    var o = {x: source.x0, y: source.y0};
                    return childdiagonal({source: o, target: o});
                })
                .transition()
                .duration(duration)
                .attr("d", childdiagonal);

            // Transition links to their new position.
            link.transition()
                .duration(duration)
                .attr("d", childdiagonal);

            // Transition exiting nodes to the parent's new position.
            link.exit().transition()
                .duration(duration)
                .attr("d", function (d) {
                    var o = {x: source.x, y: source.y};
                    return childdiagonal({source: o, target: o});
                })
                .remove();

            // Stash the old positions for transition.
            nodes.forEach(function (d) {
                d.x0 = d.x;
                d.y0 = d.y;
            });
        },

        isParent: function (node) {
            if (node.parent && node.parent != root) {
                return this.isParent(node.parent);
            } else
            // if ( node.name == 'data' || node.name == 'scale' || node.name == 'util' ) {
            if (node.parentFlag) {
                return true;
            } else {
                return false;
            }
        },

        // Toggle children.展开或关闭
        toggle: function (d) {
            if (d.children) {
                d._children = d.children;
                d.children = null;
            } else {
                d.children = d._children;
                d._children = null;
            }
            if (d.parents) {
                d._parents = d.parents;
                d.parents = null;
            } else {
                d.parents = d._parents;
                d._parents = null;
            }
        },
        //递归触发展开所有
        toggleAll: function (d) {
            if (d.children) {
                d.children.forEach(that.toggleAll);
                that.toggle(d);
            }
            if (d.parents) {
                d.parents.forEach(that.toggleAll);
                that.toggle(d);

            }
        }
    }

    return that;
}

